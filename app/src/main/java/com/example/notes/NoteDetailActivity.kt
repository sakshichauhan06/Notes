package com.example.notes

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val noteId = intent.getLongExtra("noteId", -1)

        val notesDao = NotesDatabase.getDatabase(application).notesDao()
        val repository = NotesRepository(notesDao)
        val factory = NotesViewModelFactory(repository)
        notesViewModel = NotesViewModel(repository)
        notesViewModel = androidx.lifecycle.ViewModelProvider(this,
            factory) [NotesViewModel::class.java]
        val titleText = findViewById<TextView>(R.id.note_title)
        val contextText = findViewById<TextView>(R.id.note_content)

        notesViewModel.getNoteById(noteId).observe(this) { note ->
            note?.let {
                titleText.text = note.title
                contextText.text = note.content
            }
        }
    }
}