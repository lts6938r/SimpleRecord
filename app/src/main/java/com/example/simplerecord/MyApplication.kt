package com.example.simplerecord
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
//    private val database by lazy { AppDatabase.getDatabase(this) }
//    private val noteDao: NoteDao by lazy { database.noteDao() }
//    private val bookDao: BookDao by lazy { database.bookDao() }
//    val noteRepository: NoteRepository by lazy { NoteRepository(noteDao) }
//    val bookRepository: BookRepository by lazy { BookRepository(bookDao) }
}