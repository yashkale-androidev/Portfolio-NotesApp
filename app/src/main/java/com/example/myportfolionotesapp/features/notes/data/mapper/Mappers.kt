package com.example.myportfolionotesapp.features.notes.data.mapper

import com.example.myportfolionotesapp.core.database.CategoryEntity
import com.example.myportfolionotesapp.core.database.NoteEntity
import com.example.myportfolionotesapp.core.database.NoteWithRelations
import com.example.myportfolionotesapp.core.database.TagEntity
import com.example.myportfolionotesapp.features.notes.domain.model.Category
import com.example.myportfolionotesapp.features.notes.domain.model.Note
import com.example.myportfolionotesapp.features.notes.domain.model.Tag


fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        colorHex = colorHex,
        iconName = iconName
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        colorHex = colorHex,
        iconName = iconName
    )
}

fun TagEntity.toDomain(): Tag {
    return Tag(
        id = id,
        name = name
    )
}

fun Tag.toEntity(): TagEntity {
    return TagEntity(
        id = id,
        name = name
    )
}

fun NoteWithRelations.toDomain(): Note {
    return Note(
        id = note.id,
        title = note.title,
        content = note.content,
        createdDate = note.createdDate,
        updatedDate = note.updatedDate,
        isPinned = note.isPinned,
        isArchived = note.isArchived,
        isFavorite = note.isFavorite,
        category = category?.toDomain(),
        tags = tags.map { it.toDomain() },
        reminderTime = note.reminderTime
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        createdDate = createdDate,
        updatedDate = updatedDate,
        isPinned = isPinned,
        isArchived = isArchived,
        isFavorite = isFavorite,
        categoryId = category?.id,
        reminderTime = reminderTime
    )
}
