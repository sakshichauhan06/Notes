package com.example.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notesList: List<Notes>) :
        RecyclerView.Adapter<NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.titleTextView.text = currentNote.title
        holder.contentTextView.text = currentNote.content
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun updateNotes(newNotes: List<Notes>) {
        notesList = newNotes
        notifyDataSetChanged()
    }

}























