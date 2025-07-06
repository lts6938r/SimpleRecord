package com.example.simplerecord.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplerecord.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM Book ORDER BY id DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM Book WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Update
    suspend fun updateBook(book: Book): Int

    @Delete
    suspend fun deleteBook(book: Book): Int

    @Query("DELETE FROM Book WHERE id IN (:bookIds)")
    suspend fun deleteBooksByIds(bookIds: List<Int>): Int
}
