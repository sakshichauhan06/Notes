package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavFragment : Fragment() {

    private lateinit var favNotesAdapter: NotesAdapter
    private lateinit var favNotesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.FavNotes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val favNotesDao = NotesDatabase.getDatabase(requireContext().applicationContext).notesDao()
        val favRepository = NotesRepository(favNotesDao)
        val favFactory = NotesViewModelFactory(favRepository)
        favNotesViewModel = ViewModelProvider(requireActivity(), favFactory)[NotesViewModel::class.java]

        favNotesAdapter = NotesAdapter(emptyList(), favNotesViewModel)
        recyclerView.adapter = favNotesAdapter

        favNotesViewModel.favNotes.observe(viewLifecycleOwner) { favNotes ->
            favNotesAdapter.updateNotes(favNotes)
        }
    }
}