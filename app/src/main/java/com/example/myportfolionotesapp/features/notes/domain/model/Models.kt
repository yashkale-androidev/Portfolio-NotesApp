package com.example.myportfolionotesapp.features.notes.domain.model

import java.io.Serializable

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdDate: Long,
    val updatedDate: Long,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isFavorite: Boolean = false,
    val category: Category? = null,
    val tags: List<Tag> = emptyList(),
    val reminderTime: Long? = null
) : Serializable

data class Category(
    val id: Long,
    val name: String,
    val colorHex: String,
    val iconName: String = "Folder"
) : Serializable

data class Tag(
    val id: Long,
    val name: String
) : Serializable
