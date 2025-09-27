package com.example.simplerecord.ui.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.simplerecord.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    navController: NavController,
    noteId: String?,
    noteViewModel: NoteViewModel = viewModel()
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(noteId) {
        if (noteId != null && noteId != "new") {
            coroutineScope.launch {
                val intNoteId = noteId.toIntOrNull()
                if (intNoteId != null) {
                    val existingNote = noteViewModel.noteList.value.find { it.id == intNoteId }
                    existingNote?.let {
                        title = TextFieldValue(it.title)
                        content = TextFieldValue(it.content)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (noteId == "new") "新建便签" else "编辑便签") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (title.text.isBlank() || content.text.isBlank()) {
                            Toast.makeText(context, "标题或内容不能为空", Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        if (noteId == "new") {
                            noteViewModel.addNote(title.text, content.text)
                            Toast.makeText(context, "便签已保存", Toast.LENGTH_SHORT).show()
                        } else {
                            noteId?.let { idString ->
                                val idInt = idString.toIntOrNull()
                                if (idInt != null) {
                                    noteViewModel.updateNote(idInt, title.text, content.text)
                                    Toast.makeText(context, "便签已更新", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "错误：无效的便签ID", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.Done, "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("标题") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("内容") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}