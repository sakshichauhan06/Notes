package com.example.notes

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

        tabLayout.addTab(tabLayout.newTab().setText("Notes"))
        tabLayout.addTab(tabLayout.newTab().setText("Hightlights"))
        tabLayout.addTab(tabLayout.newTab().setText("Fav"))

        viewPager2.adapter = fragmentPageAdapter

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        } )

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        // Get a reference to the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.NotesList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create an adapter (initially with an empty list)
        notesAdapter = NotesAdapter(emptyList())
        recyclerView.adapter = notesAdapter

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

        // Observe the LiveData and update the adapter when the data changes
        notesViewModel.allNotes.observe(this, Observer {
            notes ->
                notesAdapter.updateNotes(notes)
        })

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