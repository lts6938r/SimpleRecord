package com.example.simplerecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.simplerecord.ui.component.AppScaffold
import com.example.simplerecord.ui.theme.SimpleRecordTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
//        val application = application as MyApplication
//        val noteRepository = application.noteRepository

        setContent {
            SimpleRecordTheme {
                val navController = rememberNavController()
//                val noteViewModel: NoteViewModel = viewModel(
//                    factory = NoteViewModelFactory(noteRepository)
//                )
//                val notes by noteViewModel.allNotes.collectAsState()

                AppScaffold(
                    navController = navController,
//                    notes = notes
                )

            }
        }
    }
}