package com.example.notes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AllNotesFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesViewModel: NotesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notesDao = NotesDatabase.getDatabase(requireContext().applicationContext).notesDao()
        val repository = NotesRepository(notesDao)
        val factory = NotesViewModelFactory(repository)

        notesViewModel = ViewModelProvider(requireActivity(), factory) [NotesViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.NotesList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        notesAdapter = NotesAdapter(emptyList(), notesViewModel)
        recyclerView.adapter = notesAdapter



        val addNoteButton: ImageButton = view.findViewById(R.id.addNoteButton)
        addNoteButton.setOnClickListener {
            val intent = Intent(requireContext(), NoteDetailActivity::class.java)
            // Optional: pass an ID for editing or -1 for new note
            intent.putExtra("noteId", -1)
            startActivity(intent)
        }



        notesViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            notesAdapter.updateNotes(notes)
        }

    }

}