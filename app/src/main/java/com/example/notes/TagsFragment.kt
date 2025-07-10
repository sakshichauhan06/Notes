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
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var tagAdapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize ViewModel
        val dao = NotesDatabase.getDatabase(requireContext()).notesDao()
        val repository = NotesRepository(dao)
        val factory = NotesViewModelFactory(repository)
        notesViewModel = ViewModelProvider(this, factory) [NotesViewModel::class.java]

        // initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.notes_by_tag_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tagAdapter = TagAdapter(emptyList()) { selectedTag ->
            // on tag click then navigate to TagNotesFragment and pass the tag
            val action = TagsFragmentDirections.actionTagsFragmentToTagNotesFragment(selectedTag)
            findNavController().navigate(action)
        }

        recyclerView.adapter = tagAdapter

        // observe notes to extract tags
        notesViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            val tagCountMap = mutableMapOf<String, Int>()

            for (note in notes) {
                for (tag in note.tags) {
                    val lowerTag = tag.lowercase()
                    tagCountMap[lowerTag] = tagCountMap.getOrDefault(lowerTag, 0) + 1
                }
            }

            val tagList = tagCountMap.map { (tag, count) ->
                TagWithCount(tag, count)
            }

            tagAdapter.updateTags(tagList)
        }
    }

}