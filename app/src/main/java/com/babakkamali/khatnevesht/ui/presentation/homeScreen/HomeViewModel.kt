package com.babakkamali.khatnevesht.ui.presentation.homeScreen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.babakkamali.khatnevesht.data.models.NoteModel
import com.babakkamali.khatnevesht.data.models.apiCall.CreateNoteRequest
import com.babakkamali.khatnevesht.domain.NoteApiRepository
import com.babakkamali.khatnevesht.domain.NoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepo = NoteRepository(application)
    private val noteApiRepo = NoteApiRepository()
    var notesModel by mutableStateOf(emptyList<NoteModel>())

    fun getAllNotes() {
        viewModelScope.launch {
            noteRepo.getAllNotesFromRoom().collect { response ->
                notesModel = response
            }
        }
    }

    fun deleteNote(noteModel: NoteModel) {
        viewModelScope.launch {
//            noteRepo.deleteNoteFromRoom(noteModel)
        }
    }

    fun softDelete(noteModel: NoteModel) {
        viewModelScope.launch {
            noteRepo.softDeleteNoteFromRoom(noteModel.id)
        }
    }

    fun syncWithServer() {
        viewModelScope.launch {
            Log.d("SYNC", "Starting sync with server")

            // Fetch all notes from the server
            val serverNotes = noteApiRepo.getAllNotes().data ?: emptyList()
            Log.d("SYNC", "Fetched ${serverNotes.size} notes from server")

            val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

            // Fetch all local notes
            val localNotes = noteRepo.getAllNotesFromRoom().first()
            Log.d("SYNC", "Fetched ${localNotes.size} notes from local database")

            // Handle additions and updates
            for (localNote in localNotes) {
                val correspondingServerNote = serverNotes.find { it.id == localNote.serverId }

                if (localNote.serverId == 0) {
                    // New note, create on server
                    val createResponse = noteApiRepo.createNote(CreateNoteRequest(localNote.title, localNote.notes))
                    localNote.serverId = createResponse.data.id
                    noteRepo.updateNoteInRoom(localNote)
                    Log.d("SYNC", "Created new note on server with ID: ${createResponse.data.id}")
                } else if (correspondingServerNote != null) {
                    // Compare updatedAt timestamps
                    val serverDate = serverDateFormat.parse(correspondingServerNote.updatedAt)
                    when {
                        localNote.updatedAt?.compareTo(serverDate) == 1 -> {
                            // Local note is newer, update on server
                            noteApiRepo.updateNote(localNote.serverId, CreateNoteRequest(localNote.title, localNote.notes))
                            Log.d("SYNC", "Updated note on server with ID: ${localNote.serverId}")
                        }
                        localNote.updatedAt?.compareTo(serverDate) == -1 -> {
                            // Server note is newer, update locally
                            localNote.title = correspondingServerNote.title
                            localNote.notes = correspondingServerNote.description
                            noteRepo.updateNoteInRoom(localNote)
                            Log.d("SYNC", "Updated local note with ID: ${localNote.id}")
                        }
                    }
                }
            }

            // Handle new notes from server and deletions
            val localNoteServerIds = localNotes.map { it.serverId }.toSet()
            val softDeletedIds = localNotes.filter { it.isDeleted }.map { it.serverId }.toSet()

            for (serverNote in serverNotes) {
                Log.e("SYNC","serverNote.id="+serverNote.id+"localNoteServerIds.contains="+localNoteServerIds.contains(serverNote.id)+"oftDeletedIds.contains="+!softDeletedIds.contains(serverNote.id))
                if (!localNoteServerIds.contains(serverNote.id) && !softDeletedIds.contains(serverNote.id)) {
                    // New note from server, add to local database
                    val newLocalNote = NoteModel(
                        id = 0,
                        title = serverNote.title,
                        notes = serverNote.description,
                        createdAt = serverDateFormat.parse(serverNote.createdAt),
                        updatedAt = serverDateFormat.parse(serverNote.updatedAt),
                        serverId = serverNote.id
                    )
                    noteRepo.insertNoteToRoom(newLocalNote)
                    Log.d("SYNC", "Added new note from server with ID: ${serverNote.id} to local database")
                }
            }

            // Handle deletions
            val serverNoteIds = serverNotes.map { it.id }.toSet()
            for (localNote in localNotes) {
                if (!serverNoteIds.contains(localNote.serverId) || localNote.isDeleted) {
                    // Note was deleted on the server or soft-deleted locally
                    if (localNote.isDeleted) {
                        noteApiRepo.deleteNote(localNote.serverId)
                        Log.d("SYNC", "Deleted note on server with ID: ${localNote.serverId}")
                    }
                    noteRepo.deleteNoteFromRoom(localNote)
                    Log.d("SYNC", "Deleted local note with ID: ${localNote.id}")
                }
            }

            Log.d("SYNC", "Sync completed")
        }
    }


}