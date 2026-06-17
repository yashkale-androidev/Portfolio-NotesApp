package com.example.myportfolionotesapp.features.notes.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object AddEditNote : Screen("add_edit_note?noteId={noteId}") {
        fun passNoteId(noteId: Long = -1L): String {
            return "add_edit_note?noteId=$noteId"
        }
    }
    object Search : Screen("search")
    object Categories : Screen("categories")
    object Archive : Screen("archive")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
}
