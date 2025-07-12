package com.example.notes

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TagsNotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tag_notes)

        val selectedTag = intent.getStringExtra("selectedTag") ?: return

        findViewById<TextView>(R.id.selected_tag_text).text = "Tag: $selectedTag"


        val dao = NotesDatabase.getDatabase(this).notesDao()
        val repository = NotesRepository(dao)
        val factory = NotesViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory) [NotesViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.tag_notes_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = NotesAdapter(emptyList(), viewModel)
        recyclerView.adapter = adapter

        viewModel.getNoteByTag(selectedTag).observe(this) { notes ->
            adapter.updateNotes(notes)
        }

//        val fragment = TagNotesFragment()
//        val bundle = Bundle()
//        bundle.putString("selectedTag", selectedTag)
//        fragment.arguments = bundle
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()

    }

}