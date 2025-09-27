package com.example.simplerecord.ui.component

// Material 3 imports
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simplerecord.model.Note
import com.example.simplerecord.ui.navigation.Screen
import com.example.simplerecord.viewmodel.NoteViewModel

@Composable
fun NoteListScreen(
    navController: NavController,
    noteViewModel: NoteViewModel,
    notes: List<Note>
) {
    var isOneSelected by remember { mutableStateOf(false) }
    var selectedNoteIds by remember { mutableStateOf(emptySet<Int>()) }

    LaunchedEffect(isOneSelected) {
        if (!isOneSelected) {
            selectedNoteIds = emptySet()
        }
    }

    Column {
        NoteListTopAppBar(
            isOneSelected = isOneSelected,
            selectedNoteCount = selectedNoteIds.size,
            onCloseSelection = { isOneSelected = false },
            onDeleteSelected = {
                noteViewModel.deleteNotes(selectedNoteIds)
                isOneSelected = false
            }
        )


        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(notes) { note ->
                    // Check if the note's Int ID is in the selectedNoteIds set
                    val isSelected = selectedNoteIds.contains(note.id)
                    val backgroundColor = if (isSelected) Color(0xFF87CEFA) else Color.White

                    NoteListItem(
                        note = note,
                        backgroundColor = backgroundColor,
                        onLongClick = {
                            if (!isOneSelected) { // Start selection mode
                                isOneSelected = true
                                selectedNoteIds = setOf(note.id) // Add the Int ID
                            } else { // Toggle selection for current note
                                selectedNoteIds = if (isSelected) {
                                    selectedNoteIds - note.id
                                } else {
                                    selectedNoteIds + note.id
                                }
                                if (selectedNoteIds.isEmpty()) isOneSelected = false
                            }
                        },
                        onClick = {
                            if (isOneSelected) { // In selection mode, toggle selection
                                selectedNoteIds = if (isSelected) {
                                    selectedNoteIds - note.id
                                } else {
                                    selectedNoteIds + note.id
                                }
                                if (selectedNoteIds.isEmpty()) isOneSelected = false
                            } else { // Not in selection mode, navigate to edit
                                // Pass the Int ID to the route
                                navController.navigate(Screen.NoteDetail.createRoute(note.id.toString()))
                            }
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate(Screen.NoteDetail.createRoute("new")) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 70.dp, end = 10.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add new note")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListTopAppBar(
    isOneSelected: Boolean,
    selectedNoteCount: Int,
    onCloseSelection: () -> Unit,
    onDeleteSelected: () -> Unit
) {
    if (!isOneSelected) {
        TopAppBar(
            title = { Text("便签列表") },
            // Use colors property for Material 3 TopAppBar
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.White // Set the background color
//            ),
            actions = {
                Row {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, "MoreVert")
                    }
                }
            },

        )
    } else {
        TopAppBar(
            title = { Text("已选择${selectedNoteCount}条", color = Color.Blue) },
            navigationIcon = {
                IconButton(onClick = onCloseSelection) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "ArrowBack", tint = Color.Blue)
                }
            },
            // Use colors property for Material 3 TopAppBar
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.White // Set the background color
//            ),
            actions = {
                Row {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Share, "Share", tint = Color.Blue)
                    }
                    IconButton(onClick = onDeleteSelected) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.Blue)
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,
    backgroundColor: Color,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    // Material 3 ListItem uses headlineContent and supportingContent
    ListItem(
        headlineContent = { // Replaces 'text'
            Text(text = note.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        supportingContent = { // Replaces 'secondaryText'
            Text(
                text = note.content,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )
        },
        modifier = Modifier
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            )
            .background(backgroundColor)
            .height(80.dp)
    )
}