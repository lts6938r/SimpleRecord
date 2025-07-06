package com.example.simplerecord.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplerecord.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>> // Use Flow for observable queries

    @Query("SELECT * FROM Note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note? // Changed to Int for ID

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note): Int

    @Delete
    suspend fun deleteNote(note: Note): Int

    // For deleting multiple notes by ID
    @Query("DELETE FROM Note WHERE id IN (:noteIds)")
    suspend fun deleteNotesByIds(noteIds: List<Int>): Int
}