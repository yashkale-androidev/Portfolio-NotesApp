package com.example.myportfolionotesapp.features.notes.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myportfolionotesapp.core.design.components.NoteGridListFeed
import com.example.myportfolionotesapp.features.notes.presentation.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val archivedNotes by viewModel.archivedNotes.collectAsState()
    val layoutState by viewModel.layoutState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Archived Notes") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.testTag("archive_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = Modifier.testTag("archive_screen_root")
    ) { innerPadding ->
        if (archivedNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your archive is empty.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            NoteGridListFeed(
                notes = archivedNotes,
                layout = layoutState,
                onNoteClick = { note ->
                    navController.navigate("add_edit_note?noteId=${note.id}")
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
