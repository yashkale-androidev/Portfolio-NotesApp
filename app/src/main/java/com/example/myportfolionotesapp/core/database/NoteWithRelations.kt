package com.example.myportfolionotesapp.core.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NoteWithRelations(
    @Embedded val note: NoteEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NoteTagCrossRef::class,
            parentColumn = "noteId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)
