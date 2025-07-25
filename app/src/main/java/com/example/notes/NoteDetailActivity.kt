package com.example.notes

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.widget.Toolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel
    private var existingNote: Notes? = null
    private var tagList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        // import toolbar
        val toolbar = findViewById<Toolbar>(R.id.edit_notes_toolbar)
        setSupportActionBar(toolbar)

        val noteId = intent.getIntExtra("noteId", -1)

        val notesDao = NotesDatabase.getDatabase(application).notesDao()
        val repository = NotesRepository(notesDao)
        val factory = NotesViewModelFactory(repository)
        notesViewModel = ViewModelProvider(this, factory) [NotesViewModel::class.java]

        val backButton = findViewById<LinearLayout>(R.id.back_button_container)
        val saveButton = findViewById<TextView>(R.id.toolbar_save)

        val titleText = findViewById<EditText>(R.id.note_title)
        val contextText = findViewById<EditText>(R.id.note_content)
        val lastModifiedText = findViewById<TextView>(R.id.last_modified_date)

        val chipGroup = findViewById<ChipGroup>(R.id.note_tags_chip_group)

        val tagInput = findViewById<EditText>(R.id.tag_input)
        val tagAddButton = findViewById<TextView>(R.id.tag_add_button)

        // toolbar actions: back
        backButton.setOnClickListener {
            finish()
        }

        if (noteId != -1) {
            // load from DB if there is an existing note
            notesViewModel.getNoteById(noteId).observe(this) { note ->
                val updatedNote = Notes(
                    id = note.id,
                    title = note.title,
                    content = note.content,
                    createdAt = note.createdAt,
                    lastModified = System.currentTimeMillis(),
                    tags = note.tags
                )
                note?.let {
                    existingNote = it
                    titleText.setText(it.title)
                    contextText.setText(it.content)

                    // to display the last modified date
                    val formatter = SimpleDateFormat("d MMM yy HH:mm a", Locale.getDefault())
                    val formattedDate = formatter.format(Date(it.lastModified))
                    lastModifiedText.text = "Last modified: $formattedDate"

                    // to display tags as chips
                    tagList.clear()
                    tagList.addAll(it.tags)
                    displayTags(tagList, chipGroup)
                }
            }
        }

        // adding a tag
        tagAddButton.setOnClickListener {
            val tagInputText = tagInput.text.toString().trim()

            if (tagInputText.isEmpty()) {
                Toast.makeText(this, "Please enter a tag", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tag = "#$tagInputText"

            if (tagList.any { it.equals(tag, ignoreCase = true) }) {
                Toast.makeText(this, "Tag already exists", Toast.LENGTH_SHORT).show()
            } else {
                tagList.add(tag)
                displayTags(tagList, chipGroup)
            }

            tagInput.text.clear()
        }


        // save note
        saveButton.setOnClickListener {
            val title = titleText.text.toString()
            val content = contextText.text.toString()
            val createdAt = System.currentTimeMillis()
            val lastModified = System.currentTimeMillis()

            if(title.isEmpty() && content.isEmpty()) {
                Toast.makeText(this, "Cannot save empty note",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (noteId == -1) {
                // insert the new note
                val newNote = Notes(
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis(),
                    lastModified = System.currentTimeMillis(),
                    tags = tagList
                )
                notesViewModel.insert(newNote)
                Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            } else {
                // update the existing note
                existingNote?.let { note ->
                    val updateNote = Notes(
                        id = noteId,
                        title = title,
                        content = content,
                        createdAt = note.createdAt,
                        lastModified = System.currentTimeMillis(),
                        tags = tagList
                    )
                    notesViewModel.update(updateNote)
                    Toast.makeText(this, "Note Updated!", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(this, "Note not ready yet!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // we are inside the lamda funtion so return does not mean exit
                    // the activiy, it just means stop here inside this button click handler — do not continue
                    // to the rest of the logic (like saving the note or finishing the activity)
                    // also our note will not crash if the user clicks too fast without it being loaded from our DB

                }
            }

            finish() // goes back to AllNotes
        }
    }

    private fun displayTags(tags: List<String>, chipGroup: ChipGroup) {
        chipGroup.removeAllViews() // clears previous chips

        for (tag in tags) {
            val chip = Chip(this).apply {
                text = tag
                isClickable = true
                isCheckable = false
                setChipBackgroundColorResource(R.color.black)
                setTextColor(resources.getColor(R.color.white, null))

                // long press to delete
                setOnLongClickListener {
                    AlertDialog.Builder(this@NoteDetailActivity)
                        .setTitle("Remove Tag")
                        .setMessage("Do you want to delete \"$tag\"?")
                        .setPositiveButton("Delete") { _, _ ->
                            tagList.remove(tag)
                            displayTags(tagList, chipGroup)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                    true
                }
            }
            chipGroup.addView(chip)
        }
    }
}