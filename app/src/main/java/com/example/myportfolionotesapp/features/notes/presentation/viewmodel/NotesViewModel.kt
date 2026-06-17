package com.example.myportfolionotesapp.features.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myportfolionotesapp.core.datastore.AppTheme
import com.example.myportfolionotesapp.core.datastore.NotesLayout
import com.example.myportfolionotesapp.core.datastore.NotesSort
import com.example.myportfolionotesapp.core.datastore.SettingsDataStore
import com.example.myportfolionotesapp.core.notification.ReminderScheduler
import com.example.myportfolionotesapp.features.notes.domain.model.Category
import com.example.myportfolionotesapp.features.notes.domain.model.Note
import com.example.myportfolionotesapp.features.notes.domain.model.Tag
import com.example.myportfolionotesapp.features.notes.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val settingsDataStore: SettingsDataStore,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    // Preference Flows
    val themeState: StateFlow<AppTheme> = settingsDataStore.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.SYSTEM)

    val layoutState: StateFlow<NotesLayout> = settingsDataStore.layoutFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesLayout.GRID)

    val sortState: StateFlow<NotesSort> = settingsDataStore.sortFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesSort.NEWEST_FIRST)

    // Data Flows from Repository
    val categories: StateFlow<List<Category>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tags: StateFlow<List<Tag>> = repository.getTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _rawActiveNotes = repository.getActiveNotes()
    private val _rawArchivedNotes = repository.getArchivedNotes()
    private val _rawFavoriteNotes = repository.getFavoriteNotes()

    // Filtering State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    private val _selectedTag = MutableStateFlow<Tag?>(null)
    val selectedTag = _selectedTag.asStateFlow()

    // Active, Sorted, Filtered Notes Flow
    val activeNotes: StateFlow<List<Note>> = combine(
        _rawActiveNotes,
        _searchQuery,
        _selectedCategoryId,
        _selectedTag,
        sortState
    ) { notes, query, categoryId, tag, sort ->
        filterAndSortNotes(notes, query, categoryId, tag, sort)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Favorite Notes Flow
    val favoriteNotes: StateFlow<List<Note>> = combine(
        _rawFavoriteNotes,
        _searchQuery,
        sortState
    ) { notes, query, sort ->
        filterAndSortNotes(notes, query, null, null, sort)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Archived Notes Flow
    val archivedNotes: StateFlow<List<Note>> = combine(
        _rawArchivedNotes,
        _searchQuery,
        sortState
    ) { notes, query, sort ->
        filterAndSortNotes(notes, query, null, null, sort)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Helper to filter and sort
    private fun filterAndSortNotes(
        notes: List<Note>,
        query: String,
        categoryId: Long?,
        tag: Tag?,
        sort: NotesSort
    ): List<Note> {
        // We filter notes first
        var filtered = notes.filter { note ->
            val matchesQuery = note.title.contains(query, ignoreCase = true) || 
                               note.content.contains(query, ignoreCase = true)
            val matchesCategory = categoryId == null || note.category?.id == categoryId
            val matchesTag = tag == null || note.tags.any { it.name.equals(tag.name, ignoreCase = true) }
            matchesQuery && matchesCategory && matchesTag
        }

        // Then we sort
        filtered = when (sort) {
            NotesSort.NEWEST_FIRST -> filtered.sortedByDescending { it.createdDate }
            NotesSort.OLDEST_FIRST -> filtered.sortedBy { it.createdDate }
            NotesSort.ALPHABETICAL -> filtered.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.title })
            NotesSort.LAST_UPDATED -> filtered.sortedByDescending { it.updatedDate }
        }

        return filtered
    }

    // Setters for search and filters
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }

    fun selectTag(tag: Tag?) {
        _selectedTag.value = tag
    }

    // Note operations
    fun createNote(
        title: String,
        content: String,
        categoryId: Long?,
        tags: List<Tag>,
        isPinned: Boolean = false,
        isFavorite: Boolean = false,
        reminderTime: Long? = null,
        onComplete: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val matchedCategory = categories.value.find { it.id == categoryId }
            val now = System.currentTimeMillis()
            val note = Note(
                title = title,
                content = content,
                createdDate = now,
                updatedDate = now,
                isPinned = isPinned,
                isFavorite = isFavorite,
                category = matchedCategory,
                reminderTime = reminderTime
            )
            val insertedId = repository.insertNote(note, tags)
            if (reminderTime != null && reminderTime > System.currentTimeMillis()) {
                reminderScheduler.scheduleReminder(
                    noteId = insertedId,
                    title = title.ifBlank { "Reminder" },
                    content = content.take(60),
                    triggerTimeMillis = reminderTime
                )
            }
            onComplete(insertedId)
        }
    }

    fun updateNote(
        id: Long,
        title: String,
        content: String,
        categoryId: Long?,
        tags: List<Tag>,
        isPinned: Boolean,
        isFavorite: Boolean,
        reminderTime: Long?,
        isArchived: Boolean = false,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val originalNote = repository.getNoteById(id) ?: return@launch
            val matchedCategory = categories.value.find { it.id == categoryId }
            val updatedNote = originalNote.copy(
                title = title,
                content = content,
                category = matchedCategory,
                isPinned = isPinned,
                isFavorite = isFavorite,
                isArchived = isArchived,
                reminderTime = reminderTime,
                updatedDate = System.currentTimeMillis()
            )
            repository.updateNote(updatedNote, tags)

            // Update reminder scheduler
            if (reminderTime != null && reminderTime > System.currentTimeMillis()) {
                reminderScheduler.scheduleReminder(
                    noteId = id,
                    title = title.ifBlank { "Reminder" },
                    content = content.take(60),
                    triggerTimeMillis = reminderTime
                )
            } else {
                reminderScheduler.cancelReminder(id)
            }
            onComplete()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            reminderScheduler.cancelReminder(note.id)
            repository.deleteNote(note)
        }
    }

    fun duplicateNote(note: Note) {
        viewModelScope.launch {
            val duplicatedNote = note.copy(
                id = 0,
                title = "${note.title} (Copy)",
                createdDate = System.currentTimeMillis(),
                updatedDate = System.currentTimeMillis(),
                isPinned = false,
                reminderTime = null
            )
            repository.insertNote(duplicatedNote, note.tags)
        }
    }

    fun togglePin(note: Note) {
        viewModelScope.launch {
            val updated = note.copy(isPinned = !note.isPinned)
            repository.updateNote(updated, note.tags)
        }
    }

    fun toggleFavorite(note: Note) {
        viewModelScope.launch {
            val updated = note.copy(isFavorite = !note.isFavorite)
            repository.updateNote(updated, note.tags)
        }
    }

    fun toggleArchive(note: Note) {
        viewModelScope.launch {
            val updated = note.copy(isArchived = !note.isArchived, isPinned = false)
            repository.updateNote(updated, note.tags)
        }
    }

    // Category CRUD
    fun addCategory(name: String, colorHex: String) {
        viewModelScope.launch {
            repository.insertCategory(Category(id = 0, name = name, colorHex = colorHex))
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    // Tag Operations
    fun addGlobalTag(name: String) {
        viewModelScope.launch {
            repository.insertTag(Tag(id = 0, name = name))
        }
    }

    fun deleteGlobalTag(tag: Tag) {
        viewModelScope.launch {
            repository.deleteTag(tag)
        }
    }

    // Settings adjustments
    fun setAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            settingsDataStore.setTheme(theme)
        }
    }

    fun setNotesLayout(layout: NotesLayout) {
        viewModelScope.launch {
            settingsDataStore.setLayout(layout)
        }
    }

    fun setNotesSort(sort: NotesSort) {
        viewModelScope.launch {
            settingsDataStore.setSort(sort)
        }
    }

    // Flow note loader for editing
    fun getNoteFlow(id: Long): Flow<Note?> {
        return repository.getNoteByIdFlow(id)
    }
}
