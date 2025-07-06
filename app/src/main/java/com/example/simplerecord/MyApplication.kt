package com.example.simplerecord
import android.app.Application
import com.example.simplerecord.data.BookDao
import com.example.simplerecord.data.BookRepository
import com.example.simplerecord.data.NoteDao
import com.example.simplerecord.data.NoteRepository
import com.example.simplerecord.data.AppDatabase

class MyApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val noteDao: NoteDao by lazy { database.noteDao() }
    private val bookDao: BookDao by lazy { database.bookDao() }
    val noteRepository: NoteRepository by lazy { NoteRepository(noteDao) }
    val bookRepository: BookRepository by lazy { BookRepository(bookDao) }
}