package com.example.simplerecord.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simplerecord.data.NoteRepository
import com.example.simplerecord.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {


    val allNotes: StateFlow<List<Note>> = repository.notes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Keep active for 5 seconds after last collector
            initialValue = emptyList() // Initial state
        )


    fun getNote(id: Int) = viewModelScope.launch { // ID changed to Int
        repository.getNoteById(id)
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            // Room will auto-generate the ID when id = 0
            repository.insertNote(Note(id = 0, title = title, content = content))
        }
    }

    fun updateNote(id: Int, title: String, content: String) { // ID changed to Int
        viewModelScope.launch {
            repository.updateNote(Note(id = id, title = title, content = content))
        }
    }

    fun deleteNote(id: Int) { // ID changed to Int
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun deleteNotes(ids: Set<Int>) { // ID changed to Int
        viewModelScope.launch {
            repository.deleteNotes(ids)
        }
    }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}