package com.example.myportfolionotesapp.features.notes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myportfolionotesapp.features.notes.presentation.screens.AddEditNoteScreen
import com.example.myportfolionotesapp.features.notes.presentation.screens.ArchiveScreen
import com.example.myportfolionotesapp.features.notes.presentation.screens.CategoriesScreen
import com.example.myportfolionotesapp.features.notes.presentation.screens.FavoritesScreen
import com.example.myportfolionotesapp.features.notes.presentation.screens.HomeScreen
import com.example.myportfolionotesapp.features.notes.presentation.screens.SearchScreen
import com.example.myportfolionotesapp.features.notes.presentation.screens.SettingsScreen
import com.example.myportfolionotesapp.features.notes.presentation.screens.SplashScreen
import com.example.myportfolionotesapp.features.notes.presentation.viewmodel.NotesViewModel

@Composable
fun AppNavigation(viewModel: NotesViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.AddEditNote.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditNoteScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Search.route) {
            SearchScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Archive.route) {
            ArchiveScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Categories.route) {
            CategoriesScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
    }
}
