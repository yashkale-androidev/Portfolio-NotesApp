package com.example.myportfolionotesapp.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        NoteEntity::class,
        CategoryEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val categoryDao = database.categoryDao()
                    val tagDao = database.tagDao()

                    // Pre-populate default categories
                    val personalId = categoryDao.insertCategory(
                        CategoryEntity(name = "Personal", colorHex = "#AB47BC", iconName = "AccountCircle")
                    )
                    val workId = categoryDao.insertCategory(
                        CategoryEntity(name = "Work", colorHex = "#42A5F5", iconName = "Business")
                    )
                    val studyId = categoryDao.insertCategory(
                        CategoryEntity(name = "Study", colorHex = "#ABED6C", iconName = "School") // Lime green color label
                    )
                    categoryDao.insertCategory(
                        CategoryEntity(name = "Ideas", colorHex = "#FFCA28", iconName = "Lightbulb")
                    )
                    categoryDao.insertCategory(
                        CategoryEntity(name = "Shopping", colorHex = "#26A69A", iconName = "ShoppingCart")
                    )

                    // Pre-populate standard tags
                    val t1 = tagDao.insertTag(TagEntity(name = "Android"))
                    val t2 = tagDao.insertTag(TagEntity(name = "Kotlin"))
                    val t3 = tagDao.insertTag(TagEntity(name = "Compose"))
                    val t4 = tagDao.insertTag(TagEntity(name = "Interview"))
                }
            }
        }
    }
}
