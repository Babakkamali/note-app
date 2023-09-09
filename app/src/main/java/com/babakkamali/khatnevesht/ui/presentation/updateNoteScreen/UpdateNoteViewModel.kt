package com.babakkamali.khatnevesht.ui.presentation.updateNoteScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.babakkamali.khatnevesht.domain.NoteRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.babakkamali.khatnevesht.data.models.NoteModel
import kotlinx.coroutines.launch


class UpdateNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepo = NoteRepository(application)
    var noteModel by mutableStateOf(NoteModel(0, "", ""))

    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            noteRepo.getNoteByIdFromRoom(noteId).collect { response ->
                noteModel = response
            }
        }
    }

    fun updateNotes(noteModel: NoteModel) {
        viewModelScope.launch {
            noteRepo.updateNoteInRoom(noteModel)
        }
    }

    fun updateTitle(title: String) {
        noteModel = noteModel.copy(title = title)
    }

    fun updateNote(note: String) {
        noteModel = noteModel.copy(notes = note)
    }
}
