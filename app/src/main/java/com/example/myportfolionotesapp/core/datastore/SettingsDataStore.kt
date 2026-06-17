package com.example.myportfolionotesapp.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class AppTheme {
    SYSTEM, LIGHT, DARK
}

enum class NotesLayout {
    GRID, LIST
}

enum class NotesSort {
    NEWEST_FIRST, OLDEST_FIRST, ALPHABETICAL, LAST_UPDATED
}

class SettingsDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val THEME_KEY = stringPreferencesKey("app_theme")
        val LAYOUT_KEY = stringPreferencesKey("notes_layout")
        val SORT_KEY = stringPreferencesKey("notes_sort")
    }

    val themeFlow: Flow<AppTheme> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeStr = preferences[THEME_KEY] ?: AppTheme.SYSTEM.name
            runCatching { AppTheme.valueOf(themeStr) }.getOrDefault(AppTheme.SYSTEM)
        }

    val layoutFlow: Flow<NotesLayout> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val layoutStr = preferences[LAYOUT_KEY] ?: NotesLayout.GRID.name
            runCatching { NotesLayout.valueOf(layoutStr) }.getOrDefault(NotesLayout.GRID)
        }

    val sortFlow: Flow<NotesSort> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortStr = preferences[SORT_KEY] ?: NotesSort.NEWEST_FIRST.name
            runCatching { NotesSort.valueOf(sortStr) }.getOrDefault(NotesSort.NEWEST_FIRST)
        }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    suspend fun setLayout(layout: NotesLayout) {
        context.dataStore.edit { preferences ->
            preferences[LAYOUT_KEY] = layout.name
        }
    }

    suspend fun setSort(sort: NotesSort) {
        context.dataStore.edit { preferences ->
            preferences[SORT_KEY] = sort.name
        }
    }
}
