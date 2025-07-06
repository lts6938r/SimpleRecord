package com.example.simplerecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.simplerecord.ui.component.AppScaffold
import com.example.simplerecord.ui.theme.SimpleRecordTheme
import com.example.simplerecord.viewmodel.NoteViewModel
import com.example.simplerecord.viewmodel.NoteViewModelFactory


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = application as MyApplication
        val noteRepository = application.noteRepository

        setContent {
            SimpleRecordTheme {
                val navController = rememberNavController()
                val noteViewModel: NoteViewModel = viewModel(
                    factory = NoteViewModelFactory(noteRepository)
                )


                val notes by noteViewModel.allNotes.collectAsState()


                AppScaffold(
                    navController = navController,
                    noteViewModel = noteViewModel,
                    notes = notes
                )
            }
        }
    }
}