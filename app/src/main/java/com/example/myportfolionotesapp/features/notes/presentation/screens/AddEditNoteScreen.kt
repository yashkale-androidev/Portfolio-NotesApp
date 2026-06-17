package com.example.myportfolionotesapp.features.notes.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOff
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myportfolionotesapp.features.notes.domain.model.Category
import com.example.myportfolionotesapp.features.notes.domain.model.Tag
import com.example.myportfolionotesapp.features.notes.presentation.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: NotesViewModel,
    noteId: Long = -1L
) {
    // Basic state fields
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var isPinned by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var isArchived by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var noteTags by remember { mutableStateOf<List<Tag>>(emptyList()) }
    var reminderTime by remember { mutableStateOf<Long?>(null) }

    // Dialog/sheet toggles
    var showCategorySelector by remember { mutableStateOf(false) }
    var showTagEditor by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }

    // Text style aids
    var isBoldActive by remember { mutableStateOf(false) }
    var isItalicActive by remember { mutableStateOf(false) }

    val categoriesList by viewModel.categories.collectAsState()
    val flowNote = remember(noteId) {
        if (noteId != -1L) viewModel.getNoteFlow(noteId) else null
    }
    val loadedNoteState = flowNote?.collectAsState(initial = null)

    // Load Note content on arrival
    LaunchedEffect(loadedNoteState?.value) {
        loadedNoteState?.value?.let { note ->
            noteTitle = note.title
            noteContent = note.content
            isPinned = note.isPinned
            isFavorite = note.isFavorite
            isArchived = note.isArchived
            selectedCategoryId = note.category?.id
            noteTags = note.tags
            reminderTime = note.reminderTime
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId == -1L) "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.testTag("editor_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isPinned = !isPinned },
                        modifier = Modifier.testTag("editor_pin_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Pin",
                            tint = if (isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.testTag("editor_favorite_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFFFCA28) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            if (noteId == -1L) {
                                viewModel.createNote(
                                    title = noteTitle,
                                    content = noteContent,
                                    categoryId = selectedCategoryId,
                                    tags = noteTags,
                                    isPinned = isPinned,
                                    isFavorite = isFavorite,
                                    reminderTime = reminderTime
                                ) {
                                    navController.navigateUp()
                                }
                            } else {
                                viewModel.updateNote(
                                    id = noteId,
                                    title = noteTitle,
                                    content = noteContent,
                                    categoryId = selectedCategoryId,
                                    tags = noteTags,
                                    isPinned = isPinned,
                                    isFavorite = isFavorite,
                                    reminderTime = reminderTime,
                                    isArchived = isArchived
                                ) {
                                    navController.navigateUp()
                                }
                            }
                        },
                        modifier = Modifier.testTag("editor_save_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save Note", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        modifier = Modifier.testTag("editor_screen_root")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Note Title Input
            OutlinedTextField(
                value = noteTitle,
                onValueChange = { noteTitle = it },
                placeholder = { Text("Note Title", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("editor_title_input"),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Category and Tag info badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category button picker
                val matchedCategory = categoriesList.find { it.id == selectedCategoryId }
                val badgeColor = remember(matchedCategory) {
                    val hex = matchedCategory?.colorHex ?: "#7D5260"
                    runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(Color.Gray)
                }

                AssistChip(
                    onClick = { showCategorySelector = true },
                    label = { Text(matchedCategory?.name ?: "No Category") },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(badgeColor)
                        )
                    },
                    modifier = Modifier.testTag("editor_category_chip")
                )

                // Tags chip edit button
                AssistChip(
                    onClick = { showTagEditor = true },
                    label = { Text("${noteTags.size} Tags") },
                    leadingIcon = { Icon(Icons.Default.LocalOffer, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("editor_tags_chip")
                )

                // Reminder configure button
                AssistChip(
                    onClick = { showReminderDialog = true },
                    label = {
                        if (reminderTime == null) {
                            Text("No Reminder")
                        } else {
                            val sdf = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
                            Text(sdf.format(Date(reminderTime!!)))
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = if (reminderTime != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    modifier = Modifier.testTag("editor_reminder_chip")
                )
            }

            // formatting action aids toolbar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isBoldActive = !isBoldActive }) {
                        Icon(
                            Icons.Default.FormatBold,
                            contentDescription = "Bold Toggle",
                            tint = if (isBoldActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { isItalicActive = !isItalicActive }) {
                        Icon(
                            Icons.Default.FormatItalic,
                            contentDescription = "Italic Toggle",
                            tint = if (isItalicActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { noteContent = "$noteContent\n• " },
                        modifier = Modifier.testTag("format_bullet_button")
                    ) {
                        Icon(
                            Icons.Default.FormatListBulleted,
                            contentDescription = "Add bullet line",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { noteContent = "$noteContent\n[ ] " },
                        modifier = Modifier.testTag("format_todo_button")
                    ) {
                        Icon(
                            Icons.Default.Rule,
                            contentDescription = "Add checkbox",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Interactive Note Body Content Column / Editor
            OutlinedTextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                placeholder = { Text("Start typing notes here...", style = MaterialTheme.typography.bodyLarge) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .defaultMinSize(minHeight = 240.dp)
                    .testTag("editor_content_input"),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isBoldActive) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (isItalicActive) FontStyle.Italic else FontStyle.Normal
                )
            )

            if (noteId != -1L) {
                Button(
                    onClick = {
                        isArchived = !isArchived
                        viewModel.updateNote(
                            id = noteId,
                            title = noteTitle,
                            content = noteContent,
                            categoryId = selectedCategoryId,
                            tags = noteTags,
                            isPinned = isPinned,
                            isFavorite = isFavorite,
                            reminderTime = reminderTime,
                            isArchived = isArchived
                        ) {
                            navController.navigateUp()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isArchived) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isArchived) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("editor_toggle_archive_button")
                ) {
                    Icon(
                        imageVector = if (isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isArchived) "Send Note to Notebook (Unarchive)" else "Archive Note")
                }
            }
        }

        // Category Selection Dialog
        if (showCategorySelector) {
            CategorySelectorDialog(
                categories = categoriesList,
                selectedId = selectedCategoryId,
                onDismiss = { showCategorySelector = false },
                onSelect = { id ->
                    selectedCategoryId = id
                    showCategorySelector = false
                }
            )
        }

        // Tag Editor Dialog
        if (showTagEditor) {
            TagEditorDialog(
                currentTags = noteTags,
                onDismiss = { showTagEditor = false },
                onTagsUpdated = { updatedList ->
                    noteTags = updatedList
                    showTagEditor = false
                }
            )
        }

        // Reminder Scheduler Dialog
        if (showReminderDialog) {
            ReminderSchedulerDialog(
                currentReminder = reminderTime,
                onDismiss = { showReminderDialog = false },
                onSave = { selectedTime ->
                    reminderTime = selectedTime
                    showReminderDialog = false
                }
            )
        }
    }
}

@Composable
fun CategorySelectorDialog(
    categories: List<Category>,
    selectedId: Long?,
    onDismiss: () -> Unit,
    onSelect: (Long?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Notebook Category") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(null) }
                        .padding(12.dp)
                        .testTag("picker_category_none"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("No category", modifier = Modifier.weight(1f))
                    RadioButton(selected = selectedId == null, onClick = { onSelect(null) })
                }

                categories.forEach { category ->
                    val color = remember(category.colorHex) {
                        runCatching { Color(android.graphics.Color.parseColor(category.colorHex)) }.getOrDefault(Color.Gray)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(category.id) }
                            .padding(12.dp)
                            .testTag("picker_category_${category.name.lowercase()}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(color)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(category.name, modifier = Modifier.weight(1f))
                        RadioButton(selected = selectedId == category.id, onClick = { onSelect(category.id) })
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier.testTag("category_selector_dialog")
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TagEditorDialog(
    currentTags: List<Tag>,
    onDismiss: () -> Unit,
    onTagsUpdated: (List<Tag>) -> Unit
) {
    var tagInput by remember { mutableStateOf("") }
    val tagsList = remember { mutableStateListOf<Tag>().apply { addAll(currentTags) } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Tags") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = tagInput,
                        onValueChange = { tagInput = it },
                        label = { Text("Add Tag Name") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_tag_text_input")
                    )
                    Button(
                        onClick = {
                            if (tagInput.isNotBlank()) {
                                val trimmed = tagInput.trim().replace("#", "")
                                if (tagsList.none { it.name.equals(trimmed, ignoreCase = true) }) {
                                    tagsList.add(Tag(id = 0, name = trimmed))
                                }
                                tagInput = ""
                            }
                        },
                        enabled = tagInput.isNotBlank(),
                        modifier = Modifier.testTag("dialog_tag_add_confirm_button")
                    ) {
                        Text("Add")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Applied Tags",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Tags listing flowing beautifully
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (tagsList.isEmpty()) {
                        Text(
                            "No tags applied. Build organize tags.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    } else {
                        tagsList.forEach { tag ->
                            InputChip(
                                selected = true,
                                onClick = { tagsList.remove(tag) },
                                label = { Text("#${tag.name}") },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Cancel,
                                        contentDescription = "Remove tag",
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                modifier = Modifier.testTag("dialog_applied_tag_${tag.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onTagsUpdated(tagsList.toList()) },
                modifier = Modifier.testTag("dialog_tag_save")
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier.testTag("tag_editor_dialog")
    )
}

@Composable
fun ReminderSchedulerDialog(
    currentReminder: Long?,
    onDismiss: () -> Unit,
    onSave: (Long?) -> Unit
) {
    val offsets = listOf(
        Pair("In 30 Minutes", 30 * 60 * 1000L),
        Pair("In 1 Hour", 60 * 60 * 1000L),
        Pair("In 4 Hours", 4 * 60 * 60 * 1000L),
        Pair("Tomorrow Morning", 24 * 60 * 60 * 1000L)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Note Reminder") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                offsets.forEach { pair ->
                    Button(
                        onClick = {
                            val trigger = System.currentTimeMillis() + pair.second
                            onSave(trigger)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reminder_offset_${pair.first.replace(" ", "_").lowercase()}"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Text(pair.first)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (currentReminder != null) {
                    Button(
                        onClick = { onSave(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reminder_remove_button")
                    ) {
                        Icon(Icons.Default.AlarmOff, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Remove Active Reminder")
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier.testTag("reminder_dialog")
    )
}
