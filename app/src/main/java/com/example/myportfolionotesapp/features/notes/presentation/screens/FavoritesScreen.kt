package com.example.myportfolionotesapp.features.notes.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myportfolionotesapp.core.design.components.NoteGridListFeed
import com.example.myportfolionotesapp.features.notes.presentation.navigation.Screen
import com.example.myportfolionotesapp.features.notes.presentation.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val favoriteNotes by viewModel.favoriteNotes.collectAsState()
    val layoutState by viewModel.layoutState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Notes") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.testTag("favorites_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = Modifier.testTag("favorites_screen_root")
    ) { innerPadding ->
        if (favoriteNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No favorite notes yet.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            NoteGridListFeed(
                notes = favoriteNotes,
                layout = layoutState,
                onNoteClick = { note ->
                    navController.navigate(Screen.AddEditNote.passNoteId(note.id))
                },
                onPinToggle = { note -> viewModel.togglePin(note) },
                onFavoriteToggle = { note -> viewModel.toggleFavorite(note) },
                onArchiveToggle = { note -> viewModel.toggleArchive(note) },
                onDuplicate = { note -> viewModel.duplicateNote(note) },
                onDelete = { note -> viewModel.deleteNote(note) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
