package com.example.simplerecord.data

import com.example.simplerecord.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {


    val notes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: Int): Note? { // Changed ID to Int
        return noteDao.getNoteById(id)
    }

    suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note): Int {
        return noteDao.updateNote(note)
    }

    suspend fun deleteNote(noteId: Int): Int { // Changed ID to Int

        val noteToDelete = getNoteById(noteId) // Fetch the note by ID
        return if (noteToDelete != null) {
            noteDao.deleteNote(noteToDelete) // Delete by object
        } else {
            0 // Note not found
        }
    }

    suspend fun deleteNotes(noteIds: Set<Int>): Int { // Changed ID to Int
        return noteDao.deleteNotesByIds(noteIds.toList())
    }
}