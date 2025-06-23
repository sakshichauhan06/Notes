package com.example.notes

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class NotesAdapter(
    private var notesList: List<Notes>,
    private val notesViewModel: NotesViewModel)
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

            // set the heart
            if(note.isFavorite) {
                holder.favBtn.setIconResource(R.drawable.ic_favorite)
                holder.favBtn.setIconTintResource(R.color.heart_red)
            } else {
                holder.favBtn.setIconResource(R.drawable.ic_favorite_border)
                holder.favBtn.setIconTintResource(R.color.heart_border_red)
            }

            // make a note fav or unfav
            holder.favBtn.setOnClickListener {
                val newFavoriteState = !note.isFavorite
                val updatedNote = note.copy(isFavorite = newFavoriteState)

                notesViewModel.update(updatedNote)

                if (newFavoriteState) {
                    holder.favBtn.setIconResource(R.drawable.ic_favorite)
                    holder.favBtn.setIconTintResource(R.color.heart_red)
                    Toast.makeText(holder.itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show()
                } else {
                    holder.favBtn.setIconResource(R.drawable.ic_favorite_border)
                    holder.favBtn.setIconTintResource(R.color.heart_border_red)
                    Toast.makeText(holder.itemView.context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }
            }

            // delete or share
            holder.itemView.setOnLongClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Choose an option")
                    .setItems(arrayOf("Delete", "Share")) { dialog, which ->
                        when (which) {
                            0 -> {
                                AlertDialog.Builder(holder.itemView.context)
                                    .setTitle("Delete Note")
                                    .setMessage("Are you sure you want to delete this note?")
                                    .setPositiveButton("Yes") { dialog, which ->
                                        notesViewModel.delete(note)
                                        Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
                                    }
                                    .setNegativeButton("No", null)
                                    .show()
                            }
                            1 -> {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.content}")
                                    type = "text/plain"
                                }
                                val context = holder.itemView.context
                                context.startActivity(Intent.createChooser(intent, "Share via"))
                            }
                        }
                    }.show()
                true
            }

            // opens an existing note
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, NoteDetailActivity::class.java)
                intent.putExtra("noteId", note.id)
                context.startActivity(intent)
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



























