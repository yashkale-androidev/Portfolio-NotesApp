package com.example.myportfolionotesapp.features.notes.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myportfolionotesapp.R
import com.example.myportfolionotesapp.core.datastore.NotesLayout
import com.example.myportfolionotesapp.core.design.components.NoteGridListFeed
import com.example.myportfolionotesapp.features.notes.presentation.navigation.Screen
import com.example.myportfolionotesapp.features.notes.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val activeNotes by viewModel.activeNotes.collectAsState()
    val categoriesList by viewModel.categories.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val layoutState by viewModel.layoutState.collectAsState()
    val sortState by viewModel.sortState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Portfolio Notes",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("My Notes") },
                    selected = true,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Favorites") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            navController.navigate(Screen.Favorites.route)
                        }
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .testTag("drawer_favorites")
                )

                NavigationDrawerItem(
                    icon = { Icon(painter = painterResource(R.drawable.ic_archive), contentDescription = null) },
                    label = { Text("Archive") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            navController.navigate(Screen.Archive.route)
                        }
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .testTag("drawer_archive")
                )

                NavigationDrawerItem(
                    icon = { Icon(painter = painterResource(R.drawable.ic_notecategory), contentDescription = null) },
                    label = { Text("Notebook Categories") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            navController.navigate(Screen.Categories.route)
                        }
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .testTag("drawer_categories")
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            navController.navigate(Screen.Settings.route)
                        }
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .testTag("drawer_settings")
                )

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Clean Jetpack Compose Template",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp)
                )
            }
        },
        modifier = Modifier.testTag("home_drawer_root")
    ) {
        Scaffold(
            topBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Search bar overlay as custom top header container
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            },
                            modifier = Modifier.testTag("drawer_open_button")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }

                        // Elegant Search Bar card container
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clickable { navController.navigate(Screen.Search.route) }
                                .testTag("search_trigger_bar"),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color(0xFFDDE2EF)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Search your notes",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.8f
                                        )
                                    )
                                }

                                // Sleek Interface JD Profile Avatar representation from design HTML
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "JD",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Quick layout toggle button
                        IconButton(
                            onClick = {
                                viewModel.setNotesLayout(
                                    if (layoutState == NotesLayout.GRID) NotesLayout.LIST else NotesLayout.GRID
                                )
                            },
                            modifier = Modifier.testTag("layout_toggle_button")
                        ) {
                            Icon(
                                painter = if (layoutState == NotesLayout.GRID) painterResource(R.drawable.ic_list) else painterResource(R.drawable.ic_grid_view),
                                contentDescription = "Toggle layout",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Horizontal scrolling filters bar (Notebook Categories)
                    if (categoriesList.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedCategoryId == null,
                                    onClick = { viewModel.selectCategory(null) },
                                    label = { Text("All Notebooks") },
                                    modifier = Modifier.testTag("filter_category_all")
                                )
                            }
                            items(categoriesList, key = { it.id }) { category ->
                                FilterChip(
                                    selected = selectedCategoryId == category.id,
                                    onClick = {
                                        if (selectedCategoryId == category.id) {
                                            viewModel.selectCategory(null)
                                        } else {
                                            viewModel.selectCategory(category.id)
                                        }
                                    },
                                    label = { Text(category.name) },
                                    modifier = Modifier.testTag("filter_category_${category.name.lowercase()}")
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                val isDarkTheme = isSystemInDarkTheme()
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddEditNote.passNoteId(-1L)) },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .testTag("add_note_fab")
                        .then(
                            if (isDarkTheme) Modifier
                            else Modifier.border(1.dp, Color(0xFFBAC8E3), RoundedCornerShape(16.dp))
                        )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
                }
            },
            modifier = Modifier.testTag("home_screen_root")
        ) { innerPadding ->
            if (activeNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No notes found in this Notebook.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap + to capture your first idea!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                NoteGridListFeed(
                    notes = activeNotes,
                    layout = layoutState,
                    onNoteClick = { note ->
                        navController.navigate(Screen.AddEditNote.passNoteId(note.id))
                    },
                    onPinToggle = { note -> viewModel.togglePin(note) },
                    onFavoriteToggle = { note -> viewModel.toggleFavorite(note) },
                    onArchiveToggle = { note -> viewModel.toggleArchive(note) },
                    onDuplicate = { note -> viewModel.duplicateNote(note) },
                    onDelete = { note -> viewModel.deleteNote(note) },
                    showSections = true,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
