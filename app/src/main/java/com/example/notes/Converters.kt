package com.example.notes

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromTagsList(tags: List<String>): String {
        return tags.joinToString (",")
    }

    @TypeConverter
    fun toTagsList(data: String): List<String> {
        return if (data.isEmpty()) emptyList()
        else data.split(",")
    }

}