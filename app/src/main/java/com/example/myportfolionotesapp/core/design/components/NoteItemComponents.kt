package com.example.myportfolionotesapp.core.design.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myportfolionotesapp.R
import com.example.myportfolionotesapp.core.datastore.NotesLayout
import com.example.myportfolionotesapp.features.notes.domain.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteGridListFeed(
    notes: List<Note>,
    layout: NotesLayout,
    onNoteClick: (Note) -> Unit,
    onPinToggle: (Note) -> Unit,
    onFavoriteToggle: (Note) -> Unit,
    onArchiveToggle: (Note) -> Unit,
    onDuplicate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    modifier: Modifier = Modifier,
    showSections: Boolean = false
) {
    val pinnedNotes = remember(notes, showSections) {
        if (showSections) notes.filter { it.isPinned } else emptyList()
    }
    val generalNotes = remember(notes, showSections) {
        if (showSections) notes.filter { !it.isPinned } else notes
    }

    if (layout == NotesLayout.GRID) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (showSections && pinnedNotes.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "PINNED NOTES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                items(pinnedNotes, key = { it.id }) { note ->
                    NoteItemCard(
                        note = note,
                        onClick = { onNoteClick(note) },
                        onPinToggle = { onPinToggle(note) },
                        onFavoriteToggle = { onFavoriteToggle(note) },
                        onArchiveToggle = { onArchiveToggle(note) },
                        onDuplicate = { onDuplicate(note) },
                        onDelete = { onDelete(note) }
                    )
                }
                if (generalNotes.isNotEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = "OTHERS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
                        )
                    }
                }
            }
            items(generalNotes, key = { it.id }) { note ->
                NoteItemCard(
                    note = note,
                    onClick = { onNoteClick(note) },
                    onPinToggle = { onPinToggle(note) },
                    onFavoriteToggle = { onFavoriteToggle(note) },
                    onArchiveToggle = { onArchiveToggle(note) },
                    onDuplicate = { onDuplicate(note) },
                    onDelete = { onDelete(note) }
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (showSections && pinnedNotes.isNotEmpty()) {
                item {
                    Text(
                        text = "PINNED NOTES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                items(pinnedNotes, key = { it.id }) { note ->
                    NoteItemCard(
                        note = note,
                        onClick = { onNoteClick(note) },
                        onPinToggle = { onPinToggle(note) },
                        onFavoriteToggle = { onFavoriteToggle(note) },
                        onArchiveToggle = { onArchiveToggle(note) },
                        onDuplicate = { onDuplicate(note) },
                        onDelete = { onDelete(note) }
                    )
                }
                if (generalNotes.isNotEmpty()) {
                    item {
                        Text(
                            text = "OTHERS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
                        )
                    }
                }
            }
            items(generalNotes, key = { it.id }) { note ->
                NoteItemCard(
                    note = note,
                    onClick = { onNoteClick(note) },
                    onPinToggle = { onPinToggle(note) },
                    onFavoriteToggle = { onFavoriteToggle(note) },
                    onArchiveToggle = { onArchiveToggle(note) },
                    onDuplicate = { onDuplicate(note) },
                    onDelete = { onDelete(note) }
                )
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItemCard(
    note: Note,
    onClick: () -> Unit,
    onPinToggle: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onArchiveToggle: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedMenu by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val dateString = remember(note.updatedDate) { formatter.format(Date(note.updatedDate)) }

    val categoryColor = remember(note.category?.colorHex) {
        val hex = note.category?.colorHex ?: "#7D5260"
        runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(Color.Gray)
    }

    val isDark = isSystemInDarkTheme()
    val combinedText = remember(note.title, note.content) {
        (note.title + " " + note.content).lowercase()
    }
    val isLavenderType = remember(combinedText) {
        combinedText.contains("work") || combinedText.contains("project") || combinedText.contains("design") || combinedText.contains("idea") || combinedText.contains("architect")
    }
    val isYellowType = remember(combinedText) {
        combinedText.contains("shop") || combinedText.contains("buy") || combinedText.contains("grocer") || combinedText.contains("todo") || combinedText.contains("milk") || combinedText.contains("egg") || combinedText.contains("chore") || combinedText.contains("task")
    }

    val containerBg: Color
    val borderStrokeColor: Color
    val titleTextColor: Color
    val contentTextColor: Color
    val tagBg: Color
    val tagText: Color

    if (note.isPinned) {
        containerBg = MaterialTheme.colorScheme.primaryContainer
        borderStrokeColor = if (isDark) Color(0xFF3B4758) else Color(0xFFBAC8E3)
        titleTextColor = MaterialTheme.colorScheme.primary
        contentTextColor = if (isDark) MaterialTheme.colorScheme.onPrimaryContainer else Color(0xFF2E3135)
        tagBg = if (isDark) Color.Black.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.5f)
        tagText = MaterialTheme.colorScheme.primary
    } else if (isLavenderType && !isDark) {
        containerBg = Color(0xFFE7E0FF)
        borderStrokeColor = Color(0xFFCBC4E8)
        titleTextColor = Color(0xFF1C1B3F)
        contentTextColor = Color(0xFF1C1B3F).copy(alpha = 0.85f)
        tagBg = Color.White.copy(alpha = 0.5f)
        tagText = Color(0xFF1C1B3F)
    } else if (isYellowType && !isDark) {
        containerBg = Color(0xFFF2F3BB)
        borderStrokeColor = Color(0xFFDCE09D)
        titleTextColor = Color(0xFF1C1D00)
        contentTextColor = Color(0xFF1C1D00).copy(alpha = 0.85f)
        tagBg = Color.White.copy(alpha = 0.5f)
        tagText = Color(0xFF1C1D00)
    } else {
        containerBg = MaterialTheme.colorScheme.surface
        borderStrokeColor = if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.25f) else Color(0xFFE3E2E6)
        titleTextColor = MaterialTheme.colorScheme.onSurface
        contentTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        tagBg = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        tagText = MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = { expandedMenu = true }
            )
            .testTag("note_card_${note.id}"),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, borderStrokeColor),
        colors = CardDefaults.cardColors(
            containerColor = containerBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Category Tag/Indicator
                note.category?.let { cat ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(categoryColor.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(categoryColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = cat.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Title & Pin/Favorite Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.title.ifBlank { "Untitled" },
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = titleTextColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (note.isPinned) {
                            Icon(
                                painter = painterResource(R.drawable.ic_push_pin),
                                contentDescription = "Pinned",
                                tint = Color(0xFFFF0000),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (note.isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favorite",
                                tint = Color(0xFFFFCA28),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (note.reminderTime != null) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Reminder Set",
                                tint = titleTextColor.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Content description
                Text(
                    text = note.content.ifBlank { "No content" },
                    fontSize = 13.sp,
                    color = contentTextColor,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tags flow row
                if (note.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        note.tags.take(3).forEach { tag ->
                            Text(
                                text = "#${tag.name}",
                                fontSize = 10.sp,
                                color = tagText,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(tagBg)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        if (note.tags.size > 3) {
                            Text(
                                text = "+${note.tags.size - 3}",
                                fontSize = 10.sp,
                                color = contentTextColor.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Updated Date String Label
                Text(
                    text = dateString,
                    fontSize = 11.sp,
                    color = contentTextColor.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall
                )
            }


            // Dropdown option menu for the Note Item
            DropdownMenu(
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(if (note.isPinned) "Unpin" else "Pin Note") },
                    onClick = {
                        onPinToggle()
                        expandedMenu = false
                    },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                DropdownMenuItem(
                    text = { Text(if (note.isFavorite) "Remove Favorite" else "Mark Favorite") },
                    onClick = {
                        onFavoriteToggle()
                        expandedMenu = false
                    },
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) }
                )
                DropdownMenuItem(
                    text = { Text(if (note.isArchived) "Restore Note" else "Archive Note") },
                    onClick = {
                        onArchiveToggle()
                        expandedMenu = false
                    },
                    leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) }
                )
                DropdownMenuItem(
                    text = { Text("Duplicate") },
                    onClick = {
                        onDuplicate()
                        expandedMenu = false
                    },
                    leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        onDelete()
                        expandedMenu = false
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
                )
            }
        }
    }
}
