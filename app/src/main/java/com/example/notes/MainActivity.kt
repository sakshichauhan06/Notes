package com.example.notes

import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Locale

fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) return this
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}


class MainActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var fragmentPageAdapter: FragmentPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)

        fragmentPageAdapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = fragmentPageAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "notes".capitalizeFirstLetter()
                1 -> "highlights".capitalizeFirstLetter()
                2 -> "fav".capitalizeFirstLetter()
                else -> "Tab ${position + 1}"
            }
        }.attach()

        // Get a reference to NotesDao
        val notesDao = NotesDatabase.getDatabase(application).notesDao()

        // OPen a repository
        val repository = NotesRepository(notesDao)

        // create the ViewModelFactory
        val factory = NotesViewModelFactory(repository)

        // Create a NotesViewModelFactory
        notesViewModel = NotesViewModel(repository)

        // Get the ViewModel using the factory
        notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]


        // get a reference to the addNoteButton
        val addNoteButton: ImageButton = findViewById(R.id.addNoteButton)

        // set a click listener on the button
        addNoteButton.setOnClickListener {
            // create a sample note
            val sampleNote = Notes(title = "Shopping List", content = "This is a sample note added at ${System.currentTimeMillis()}")
            // insert the sample note into the database
            notesViewModel.insert(sampleNote)
            // optional: show a toast message
            Toast.makeText(this, "Sample note added!", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}