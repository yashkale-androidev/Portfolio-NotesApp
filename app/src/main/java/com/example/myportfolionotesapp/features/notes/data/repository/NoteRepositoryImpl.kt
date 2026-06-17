package com.example.myportfolionotesapp.features.notes.data.repository


import com.example.myportfolionotesapp.core.database.CategoryDao
import com.example.myportfolionotesapp.core.database.NoteDao
import com.example.myportfolionotesapp.core.database.NoteTagCrossRef
import com.example.myportfolionotesapp.core.database.TagDao
import com.example.myportfolionotesapp.core.database.TagEntity
import com.example.myportfolionotesapp.features.notes.data.mapper.toDomain
import com.example.myportfolionotesapp.features.notes.data.mapper.toEntity
import com.example.myportfolionotesapp.features.notes.domain.model.Category
import com.example.myportfolionotesapp.features.notes.domain.model.Note
import com.example.myportfolionotesapp.features.notes.domain.model.Tag
import com.example.myportfolionotesapp.features.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao
) : NoteRepository {

    override fun getActiveNotes(): Flow<List<Note>> {
        return noteDao.getActiveNotesWithRelations().map { notes ->
            notes.map { it.toDomain() }
        }
    }

    override fun getArchivedNotes(): Flow<List<Note>> {
        return noteDao.getArchivedNotesWithRelations().map { notes ->
            notes.map { it.toDomain() }
        }
    }

    override fun getFavoriteNotes(): Flow<List<Note>> {
        return noteDao.getFavoriteNotesWithRelations().map { notes ->
            notes.map { it.toDomain() }
        }
    }

    override fun getNoteByIdFlow(id: Long): Flow<Note?> {
        return noteDao.getNoteWithRelationsByIdFlow(id).map { note ->
            note?.toDomain()
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteWithRelationsById(id)?.toDomain()
    }

    override suspend fun insertNote(note: Note, tagList: List<Tag>): Long {
        val noteId = noteDao.insertNote(note.toEntity())
        
        // Update tags
        tagDao.deleteNoteTagCrossRefsByNoteId(noteId)
        for (tag in tagList) {
            val dbTag = tagDao.getTagByName(tag.name)
            val finalTagId = if (dbTag != null) {
                dbTag.id
            } else {
                tagDao.insertTag(TagEntity(name = tag.name))
            }
            tagDao.insertNoteTagCrossRef(NoteTagCrossRef(noteId, finalTagId))
        }
        return noteId
    }

    override suspend fun updateNote(note: Note, tagList: List<Tag>) {
        noteDao.updateNote(note.toEntity())
        val noteId = note.id
        
        // Update tags
        tagDao.deleteNoteTagCrossRefsByNoteId(noteId)
        for (tag in tagList) {
            val dbTag = tagDao.getTagByName(tag.name)
            val finalTagId = if (dbTag != null) {
                dbTag.id
            } else {
                tagDao.insertTag(TagEntity(name = tag.name))
            }
            tagDao.insertNoteTagCrossRef(NoteTagCrossRef(noteId, finalTagId))
        }
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }

    // Categories
    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }

    // Tags
    override fun getTags(): Flow<List<Tag>> {
        return tagDao.getAllTags().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTagByName(name: String): Tag? {
        return tagDao.getTagByName(name)?.toDomain()
    }

    override suspend fun insertTag(tag: Tag): Long {
        return tagDao.insertTag(tag.toEntity())
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDao.deleteTag(tag.toEntity())
    }
}
