package com.example.notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Notes::class], version = 1)  // we have 1 Notes table & the class which defines what it should look like
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}