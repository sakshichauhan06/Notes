package com.example.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel(){
    val allNotes: LiveData<List<Notes>> = repository.allNotes.asLiveData()
    val favNotes: LiveData<List<Notes>> = repository.favNotes.asLiveData()

    fun getNoteById(id: Int): LiveData<Notes> = repository.getNoteById(id).asLiveData()

    fun insert(notes: Notes) = viewModelScope.launch {
        repository.insert(notes)
    }

    fun update(notes: Notes) = viewModelScope.launch {
        repository.update(notes)
    }

    fun delete(notes: Notes) = viewModelScope.launch {
        repository.delete(notes)
    }
}

