package com.example.simplerecord.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LOG_TAG
import com.example.simplerecord.data.NoteRepository
import com.example.simplerecord.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel  @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    private  val TAG = "NoteViewModel"

    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList: StateFlow<List<Note>> = _noteList
    private val _isAllNotesLoading = MutableStateFlow(true)
    val isAllNotesLoading: StateFlow<Boolean> = _isAllNotesLoading

    private val _isCurrentNoteLoading = MutableStateFlow(true)
    val isCurrentNoteLoading: StateFlow<Boolean> = _isCurrentNoteLoading

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()


    init {
        getALLNotes()
        Log.i(TAG, "NoteViewModel Info log: ${this.hashCode()}")
    }

    fun getALLNotes() {
        viewModelScope.launch {
            _isAllNotesLoading.value = true
            noteRepository.getAllNotes().collect { newList ->
                _noteList.value = newList
                _isAllNotesLoading.value = false
            }
        }
    }
//    val allNotes: StateFlow<List<Note>> = repository.notes
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000), // Keep active for 5 seconds after last collector
//            initialValue = emptyList()
//        )


    fun getNote(id: Int) {
        viewModelScope.launch {
            _isCurrentNoteLoading.value=true
            val result= noteRepository.getNoteById(id)
            _currentNote.value = result
            _isCurrentNoteLoading.value=false
        }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            // Room will auto-generate the ID when id = 0
            noteRepository.insertNote(Note(id = 0, title = title, content = content))
        }
    }

    fun updateNote(id: Int, title: String, content: String) { // ID changed to Int
        viewModelScope.launch {
            noteRepository.updateNote(Note(id = id, title = title, content = content))
        }
    }

    fun deleteNote(id: Int) { // ID changed to Int
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

    fun deleteNotes(ids: Set<Int>) { // ID changed to Int
        viewModelScope.launch {
            noteRepository.deleteNotes(ids)
        }
    }
}

//class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return NoteViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}