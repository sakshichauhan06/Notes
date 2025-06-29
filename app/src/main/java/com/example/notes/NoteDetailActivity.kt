package com.example.notes

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notes.R.*

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_note_detail)

        setSupportActionBar(findViewById(id.edit_notes_toolbar))

        val noteId = intent.getIntExtra("noteId", -1)

        val notesDao = NotesDatabase.getDatabase(application).notesDao()
        val repository = NotesRepository(notesDao)
        val factory = NotesViewModelFactory(repository)
        notesViewModel = ViewModelProvider(this, factory) [NotesViewModel::class.java]

//        val toolbar = findViewById<Toolbar>(R.id.edit_notes_toolbar)
//        setSupportActionBar(toolbar)
        val backButton = findViewById<LinearLayout>(id.back_button_container)
        val saveButton = findViewById<TextView>(id.toolbar_save)

        val titleText = findViewById<EditText>(R.id.note_title)
        val contextText = findViewById<EditText>(R.id.note_content)

        // toolbar actions: back and save
        backButton.setOnClickListener {
            finish()
        }
        saveButton.setOnClickListener {
            val title = titleText.text.toString()
            val content = contextText.text.toString()
        }


        if (noteId != -1) {
            // load from DB if there is an existing note
            notesViewModel.getNoteById(noteId).observe(this) { note ->
                note?.let {
                    titleText.setText(it.title)
                    contextText.setText(it.content)
                }
            }
        } else {
            // create a new note
            titleText.setText("")
            contextText.setText("")
        }

        saveButton.setOnClickListener {
            val title = titleText.text.toString()
            val content = contextText.text.toString()

            if(title.isEmpty() && content.isEmpty()) {
                Toast.makeText(this, "Cannot save empty note",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (noteId == -1) {
                // insert the new note
                val newNote = Notes(title = title, content = content)
                notesViewModel.insert(newNote)
                Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            } else {
                // update the existing note
                val updateNote = Notes(id = noteId, title = title, content = content)
                notesViewModel.update(updateNote)
                Toast.makeText(this, "Note Updated!", Toast.LENGTH_SHORT).show()
            }

            finish() // goes back to AllNotes
        }
    }
}