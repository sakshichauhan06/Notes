package com.example.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert
    suspend fun insert(notes: Notes)

    @Update
    suspend fun update(notes: Notes)

    @Delete
    suspend fun delete(notes: Notes)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Notes>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNotesById(id: Int): Flow<Notes>

    @Query("SELECT * FROM notes WHERE isFavorite = 1")
    fun getFavNotes(): Flow<List<Notes>>

    @Query("SELECT * FROM notes WHERE tags LIKE '%' || :tag || '%'")
    fun getNotesByTag(tag: String): Flow<List<Notes>>
}
