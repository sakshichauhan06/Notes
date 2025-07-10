package com.example.notes

import kotlinx.coroutines.flow.Flow

class NotesRepository (private val notesDao: NotesDao) {
    val allNotes = notesDao.getAllNotes()
    val favNotes = notesDao.getFavNotes()

    fun getAllTags(): List<String> {
        val notesWithTags = notesDao.getAllNotesWithTags()
        return notesWithTags.flatMap { it.tags }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinctBy { it.lowercase() } // makes sure tags like "#Work" and "#work" are treated the same
    }


    fun getNoteById(id: Int) = notesDao.getNotesById(id)

    fun getNoteByTag(tag: String) = notesDao.getNotesByTag(tag)

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