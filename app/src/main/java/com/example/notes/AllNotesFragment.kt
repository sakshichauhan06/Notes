package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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

        val recyclerView = view.findViewById<RecyclerView>(R.id.NotesList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        notesAdapter = NotesAdapter(emptyList())
        recyclerView.adapter = notesAdapter

        val notesDao = NotesDatabase.getDatabase(requireContext().applicationContext).notesDao()
        val repository = NotesRepository(notesDao)
        val factory = NotesViewModelFactory(repository)

        notesViewModel = ViewModelProvider(requireActivity(), factory) [NotesViewModel::class.java]

        notesViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            notesAdapter.updateNotes(notes)
        }

    }

}