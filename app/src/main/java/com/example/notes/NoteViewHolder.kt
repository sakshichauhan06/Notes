package com.example.notes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleTextView: TextView = itemView.findViewById(R.id.notes_title)
    val contentTextView: TextView = itemView.findViewById(R.id.notes_content)
}