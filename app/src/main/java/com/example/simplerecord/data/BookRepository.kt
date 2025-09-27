package com.example.simplerecord.data

import com.example.simplerecord.model.Book
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {


    fun getAllBook(): Flow<List<Book>> {
        return bookDao.getAllBooks()
    }

    suspend fun insertBook(book: Book): Long {
        return bookDao.insertBook(book)
    }

    suspend fun updateBook(book: Book): Int {
        return bookDao.updateBook(book)
    }

    suspend fun deleteBook(bookId: Int): Int {
        return bookDao.deleteBook(bookId)
    }

    suspend fun getBookById(bookId: Int): Book? {
        return bookDao.getBookById(bookId)
    }

    suspend fun deleteBooks(bookIds: Set<Int>): Int {
        return bookDao.deleteBooksByIds(bookIds.toList())
    }
}