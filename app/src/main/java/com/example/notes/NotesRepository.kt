package com.example.notes

import kotlinx.coroutines.flow.Flow

class NotesRepository (private val notesDao: NotesDao) {
    val allNotes: Flow<List<Notes>> = notesDao.getAllNotes()
    val favNotes: Flow<List<Notes>> = notesDao.getFavNotes()

    fun getNoteById(id: Int): Flow<Notes> = notesDao.getNotesById(id)

    suspend fun insert(notes: Notes) {
        notesDao.insert(notes)
    }

    suspend fun update(notes: Notes) {
        notesDao.update(notes)
    }

    suspend fun delete(notes: Notes) {
        notesDao.delete(notes)
    }
}