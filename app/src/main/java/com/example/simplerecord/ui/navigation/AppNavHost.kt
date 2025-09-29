package com.example.simplerecord.ui.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.simplerecord.ui.component.BackupScreen
import com.example.simplerecord.ui.component.HomeScreen
import com.example.simplerecord.ui.component.MineScreen
import com.example.simplerecord.ui.component.NoteDetailScreen
import com.example.simplerecord.ui.component.NoteListScreen
import com.example.simplerecord.ui.component.QRCodeScannerScreen
import com.example.simplerecord.ui.component.SearchScreen
import com.example.simplerecord.ui.component.SettingsScreen
import com.example.simplerecord.ui.component.TranscriptionScreen
import com.example.simplerecord.viewmodel.NoteViewModel


sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Notes : Screen("notes")
    data object Mine : Screen("mine")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
    data object Backup : Screen("backup")
    data object Transcription : Screen("Transcription")
    data object QRCodeScanner : Screen("qrcode_scanner")
    data object AddBook : Screen("add_book") // For adding books, if implemented later
    data object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: String) = "note_detail/$noteId"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
//    notes: List<Note>,
    innerPadding: PaddingValues // Added padding parameter
) {
    val noteViewModel: NoteViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Notes.route) {
            NoteListScreen(navController = navController,noteViewModel=noteViewModel)
        }
        composable(Screen.NoteDetail.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteDetailScreen(
                navController = navController,
                noteId = noteId,
                noteViewModel=noteViewModel
            )
        }
        composable(Screen.Mine.route) {
            MineScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screen.Backup.route) {
            BackupScreen(navController = navController)
        }
        composable(Screen.Transcription.route) {
            TranscriptionScreen()
        }
        composable(Screen.QRCodeScanner.route) {
            QRCodeScannerScreen()
        }
        composable(Screen.AddBook.route) {
            Text("添加书籍内容")
        }

    }
}