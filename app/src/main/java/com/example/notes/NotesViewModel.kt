package com.example.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel(){
    val allNotes: LiveData<List<Notes>> = repository.allNotes.asLiveData()

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

class NotesViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}