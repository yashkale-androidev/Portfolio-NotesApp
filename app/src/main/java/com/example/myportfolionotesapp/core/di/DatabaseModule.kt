package com.example.myportfolionotesapp.core.di

import android.content.Context
import com.example.myportfolionotesapp.core.database.AppDatabase
import com.example.myportfolionotesapp.core.database.CategoryDao
import com.example.myportfolionotesapp.core.database.NoteDao
import com.example.myportfolionotesapp.core.database.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope
    ): AppDatabase {
        return AppDatabase.getDatabase(context, scope)
    }

    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideTagDao(db: AppDatabase): TagDao = db.tagDao()
}
