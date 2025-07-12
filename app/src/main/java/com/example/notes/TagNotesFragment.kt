package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TagNotesFragment : Fragment() {
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get selected tag from arguments
        val selectedTag = arguments?.getString("selectedTag")?:return

        // initialize ViewModel
        val dao = NotesDatabase.getDatabase(requireContext()).notesDao()
        val repository = NotesRepository(dao)
        val factory = NotesViewModelFactory(repository)
        notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

        // setup RecylerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.tag_notes_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        adapter = NotesAdapter(emptyList(), notesViewModel)
        recyclerView.adapter = adapter

        // observe notes filtered by tag
        notesViewModel.getNoteByTag(selectedTag).observe(viewLifecycleOwner)  { filteredNotes ->
            adapter.updateNotes(filteredNotes)
        }
    }

}