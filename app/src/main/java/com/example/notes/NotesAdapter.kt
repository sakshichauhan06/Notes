package com.example.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class NotesAdapter(private var notesList: List<Notes>)
    : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

        class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.notes_title)
            val content: TextView = itemView.findViewById(R.id.notes_content)
            val favBtn: MaterialButton = itemView.findViewById(R.id.favorite_button)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NotesViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
            return NotesViewHolder(view)
        }

        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
            val note = notesList[position]

            holder.title.text = note.title
            holder.content.text = note.content

            var isFav = false

            holder.favBtn.setOnClickListener {
                isFav = !isFav
                if(isFav) {
                    holder.favBtn.setIconResource(R.drawable.ic_favorite)
                    holder.favBtn.setIconTintResource(R.color.heart_red)
                    Toast.makeText(holder.itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show( )
                } else {
                    holder.favBtn.setIconResource(R.drawable.ic_favorite_border)
                    holder.favBtn.setIconTintResource(R.color.heart_border_red)
                    Toast.makeText(holder.itemView.context, "Removed from favorites", Toast.LENGTH_SHORT).show( )
                }
            }
        }

        override fun getItemCount(): Int {
            return notesList.size
        }

        fun updateNotes(newNotes: List<Notes>) {
            notesList = newNotes
            notifyDataSetChanged()
        }

    }



























