package com.example.simplerecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simplerecord.data.BookRepository
import com.example.simplerecord.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    private val _bookList = MutableStateFlow<List<Book>>(emptyList())
    val bookList: StateFlow<List<Book>> = _bookList
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getALLBooks()
    }

    fun getALLBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllBook().collect { newList ->
                _bookList.value = newList
                _isLoading.value = false
            }
        }
    }

    fun getBook(id: Int) = viewModelScope.launch { // ID changed to Int
        repository.getBookById(id)
    }

    fun deleteBook(id: Int) { // ID changed to Int
        viewModelScope.launch {
            repository.deleteBook(id)
        }
    }

    fun addBook(
        name: String,
        author: String,
        country: String,
        comment: String,
        category: String,
        cover: String,
        year: String
    ) {
        viewModelScope.launch {
            // Room will auto-generate the ID when id = 0
            repository.insertBook(
                Book(
                    id = 0,
                    name = name,
                    author = author,
                    country = country,
                    comment = comment,
                    category = category,
                    cover = cover,
                    year = year
                )
            )
        }
    }

    fun updateBook(
        id: Int,
        name: String,
        author: String,
        country: String,
        comment: String,
        category: String,
        cover: String,
        year: String
    ) {
        viewModelScope.launch {
            // Room will auto-generate the ID when id = 0
            repository.updateBook(
                Book(
                    id = id,
                    name = name,
                    author = author,
                    country = country,
                    comment = comment,
                    category = category,
                    cover = cover,
                    year = year
                )
            )
        }
    }


}

class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}