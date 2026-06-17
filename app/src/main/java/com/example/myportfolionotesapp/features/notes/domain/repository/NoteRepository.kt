package com.example.myportfolionotesapp.features.notes.domain.repository


import com.example.myportfolionotesapp.features.notes.domain.model.Category
import com.example.myportfolionotesapp.features.notes.domain.model.Note
import com.example.myportfolionotesapp.features.notes.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getActiveNotes(): Flow<List<Note>>
    fun getArchivedNotes(): Flow<List<Note>>
    fun getFavoriteNotes(): Flow<List<Note>>
    fun getNoteByIdFlow(id: Long): Flow<Note?>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note, tagList: List<Tag>): Long
    suspend fun updateNote(note: Note, tagList: List<Tag>)
    suspend fun deleteNote(note: Note)
    suspend fun deleteNoteById(id: Long)

    // Categories
    fun getCategories(): Flow<List<Category>>
    suspend fun insertCategory(category: Category): Long
    suspend fun deleteCategory(category: Category)

    // Tags
    fun getTags(): Flow<List<Tag>>
    suspend fun getTagByName(name: String): Tag?
    suspend fun insertTag(tag: Tag): Long
    suspend fun deleteTag(tag: Tag)
}
