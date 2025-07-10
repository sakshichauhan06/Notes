package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TagNotesFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var tag: String

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        // get the tag from navigation
        arguments?.let {
            tag = TagNotesFragmentArgs.fromBundle(it).tag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tag_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = NotesDatabase.getDatabase(requireContext()).notesDao()
        val repository = NotesRepository(dao)
        val factory = NotesViewModelFactory(repository)
        notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.notes_by_tag_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        notesAdapter = NotesAdapter(emptyList(), notesViewModel)
        recyclerView.adapter = notesAdapter

        notesViewModel.getNoteByTag(tag).observe(viewLifecycleOwner) { notes ->
            notesAdapter.updateNotes(notes)
    }

}