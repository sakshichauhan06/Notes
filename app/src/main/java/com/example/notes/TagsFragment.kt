package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TagsFragment : Fragment() {

    private lateinit var tagNotesAdapter: NotesAdapter
    private lateinit var tagNotesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.tags)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tagNotesDao = NotesDatabase.getDatabase(requireContext().applicationContext).notesDao()
        val tagRepository = NotesRepository(tagNotesDao)
        val tagFactory = NotesViewModelFactory(tagRepository)
        tagNotesViewModel = ViewModelProvider(requireActivity(), tagFactory)[NotesViewModel::class.java]

        tagNotesAdapter = NotesAdapter(emptyList(), tagNotesViewModel)
        recyclerView.adapter = tagNotesAdapter

        tagNotesViewModel.tagNotes.observe(viewLifecycleOwner) { notes ->
            tagNotesAdapter.updateNotes(notes)
        }
    }
}