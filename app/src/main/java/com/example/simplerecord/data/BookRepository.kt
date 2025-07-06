package com.example.simplerecord.data

import com.example.simplerecord.model.Book
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing Book data.
 * This class abstracts the data source (Room database) and provides a clean API for the ViewModel.
 */
class BookRepository(private val bookDao: BookDao) {

    /**
     * Retrieves all books from the database as a Flow.
     * Any changes to the Book table will automatically trigger new emissions from this Flow.
     */
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    /**
     * Inserts a new book into the database.
     *
     * @param book The [Book] object to insert. If its ID is 0, Room will auto-generate one.
     * @return The row ID of the newly inserted book, or -1 if an error occurred.
     */
    suspend fun insertBook(book: Book): Long {
        return bookDao.insertBook(book)
    }

    /**
     * Updates an existing book in the database.
     *
     * @param book The [Book] object to update. The ID must match an existing book.
     * @return The number of rows updated (typically 1 if successful, 0 otherwise).
     */
    suspend fun updateBook(book: Book): Int {
        return bookDao.updateBook(book)
    }

    /**
     * Deletes a specific book from the database.
     *
     * @param book The [Book] object to delete. It identifies the book by its primary key (ID).
     * @return The number of rows deleted (typically 1 if successful, 0 otherwise).
     */
    suspend fun deleteBook(book: Book): Int {
        return bookDao.deleteBook(book)
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId The unique integer ID of the book to retrieve.
     * @return The [Book] object if found, or `null` if no book with the given ID exists.
     */
    suspend fun getBookById(bookId: Int): Book? {
        return bookDao.getBookById(bookId)
    }

    /**
     * Deletes multiple books from the database by their IDs.
     *
     * @param bookIds A [Set] of integer IDs of the books to delete.
     * @return The number of rows deleted.
     */
    suspend fun deleteBooks(bookIds: Set<Int>): Int {
        return bookDao.deleteBooksByIds(bookIds.toList())
    }
}