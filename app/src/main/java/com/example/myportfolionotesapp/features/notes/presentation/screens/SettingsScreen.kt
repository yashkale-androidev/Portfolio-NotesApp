package com.example.myportfolionotesapp.features.notes.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myportfolionotesapp.core.datastore.AppTheme
import com.example.myportfolionotesapp.core.datastore.NotesLayout
import com.example.myportfolionotesapp.core.datastore.NotesSort
import com.example.myportfolionotesapp.features.notes.presentation.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val themeState by viewModel.themeState.collectAsState()
    val layoutState by viewModel.layoutState.collectAsState()
    val sortState by viewModel.sortState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.testTag("settings_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = Modifier.testTag("settings_screen_root")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme preference card
            PreferenceSectionTitle(title = "Appearance")
            ThemePreferenceCard(
                currentTheme = themeState,
                onThemeSelected = { viewModel.setAppTheme(it) }
            )

            HorizontalDivider()

            // Layout preference card
            PreferenceSectionTitle(title = "Layout & View")
            LayoutPreferenceCard(
                currentLayout = layoutState,
                onLayoutSelected = { viewModel.setNotesLayout(it) }
            )

            HorizontalDivider()

            // Sorting preference card
            PreferenceSectionTitle(title = "Default Sorting")
            SortPreferenceCard(
                currentSort = sortState,
                onSortSelected = { viewModel.setNotesSort(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()

            // About application
            Text(
                text = "Notes Portfolio v1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun PreferenceSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun ThemePreferenceCard(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AppTheme.values().forEach { theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onThemeSelected(theme) }
                        .padding(12.dp)
                        .testTag("theme_${theme.name.lowercase()}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = when (theme) {
                        AppTheme.LIGHT -> Icons.Default.Info
                        AppTheme.DARK -> Icons.Default.Lock
                        AppTheme.SYSTEM -> Icons.Default.Settings
                    }
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = when (theme) {
                            AppTheme.LIGHT -> "Light Theme"
                            AppTheme.DARK -> "Dark Theme"
                            AppTheme.SYSTEM -> "System Default"
                        },
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = currentTheme == theme,
                        onClick = { onThemeSelected(theme) }
                    )
                }
            }
        }
    }
}

@Composable
fun LayoutPreferenceCard(
    currentLayout: NotesLayout,
    onLayoutSelected: (NotesLayout) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            NotesLayout.values().forEach { layout ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLayoutSelected(layout) }
                        .padding(12.dp)
                        .testTag("layout_${layout.name.lowercase()}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = when (layout) {
                        NotesLayout.GRID -> Icons.Default.Menu
                        NotesLayout.LIST -> Icons.Default.List
                    }
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = when (layout) {
                            NotesLayout.GRID -> "Grid View (Compact)"
                            NotesLayout.LIST -> "List View (Expanded)"
                        },
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = currentLayout == layout,
                        onClick = { onLayoutSelected(layout) }
                    )
                }
            }
        }
    }
}

@Composable
fun SortPreferenceCard(
    currentSort: NotesSort,
    onSortSelected: (NotesSort) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            NotesSort.values().forEach { sort ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortSelected(sort) }
                        .padding(12.dp)
                        .testTag("sort_${sort.name.lowercase()}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = when (sort) {
                        NotesSort.NEWEST_FIRST -> Icons.Default.ArrowDropDown
                        NotesSort.OLDEST_FIRST -> Icons.Default.ArrowForward
                        NotesSort.ALPHABETICAL -> Icons.Default.List
                        NotesSort.LAST_UPDATED -> Icons.Default.Refresh
                    }
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = when (sort) {
                            NotesSort.NEWEST_FIRST -> "Newest First"
                            NotesSort.OLDEST_FIRST -> "Oldest First"
                            NotesSort.ALPHABETICAL -> "Alphabetical (A-Z)"
                            NotesSort.LAST_UPDATED -> "Recently Modified"
                        },
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = currentSort == sort,
                        onClick = { onSortSelected(sort) }
                    )
                }
            }
        }
    }
}
