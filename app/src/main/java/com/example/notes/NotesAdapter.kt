package com.example.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notesList: List<Notes>)
    : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

        class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.notes_title)
            val content: TextView = itemView.findViewById(R.id.notes_content)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NotesViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
            return NotesViewHolder(view)
        }

        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
            val note = notesList[position]
            holder.title.text = note.title
            holder.content.text = note.content
        }

        override fun getItemCount(): Int {
            return notesList.size
        }

        fun updateNotes(newNotes: List<Notes>) {
            notesList = newNotes
            notifyDataSetChanged()
        }

    }



























