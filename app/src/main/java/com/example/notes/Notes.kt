package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val createdAt: Long,
    val lastModified: Long,
    val tags: List<String>
)
