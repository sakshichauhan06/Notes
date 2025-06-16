package com.example.notes

import kotlinx.coroutines.flow.Flow

class NotesRepository (private val notesDao: NotesDao) {
    val allNotes = notesDao.getAllNotes()
    val favNotes = notesDao.getFavNotes()

    fun getNoteById(id: Int) = notesDao.getNotesById(id)

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