package com.babakkamali.khatnevesht.ui.presentation.homeScreen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.babakkamali.khatnevesht.data.daos.NoteDao
import com.babakkamali.khatnevesht.data.database.NoteDatabase
import com.babakkamali.khatnevesht.data.models.NoteModel
import com.babakkamali.khatnevesht.data.models.apiCall.CreateNoteRequest
import com.babakkamali.khatnevesht.domain.NoteApiRepository
import com.babakkamali.khatnevesht.domain.NoteRepository
import com.babakkamali.khatnevesht.exception.NoConnectivityException
import com.babakkamali.khatnevesht.ui.presentation.loginScreen.LoginUIState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepo = NoteRepository(application)
    private val noteApiRepo = NoteApiRepository()
    var notesModel by mutableStateOf(emptyList<NoteModel>())

    val uiState: MutableLiveData<HomeScreenState?> = MutableLiveData()


    fun getAllNotes() {
        viewModelScope.launch {
            noteRepo.getAllNotesFromRoom().collect { response ->
                notesModel = response
            }
        }

        uiState.value = HomeScreenState.Success
    }

//    fun deleteNote(noteModel: NoteModel) {
//        viewModelScope.launch {
//            noteRepo.deleteNoteFromRoom(noteModel)
//        }
//    }

    fun softDelete(noteModel: NoteModel) {
        viewModelScope.launch {
            noteRepo.softDeleteNoteFromRoom(noteModel.id)
        }
        syncWithServer()
    }

    fun syncWithServer() {
        viewModelScope.launch {
            try {
                uiState.value = HomeScreenState.Loading
                Log.d("SYNC", "Starting sync with server")

                // Step 1: Fetch all notes initially
                val serverNotesInitial = noteApiRepo.getAllNotes().data ?: emptyList()
                val localNotesInitial = noteRepo.getAllNotesWithSoftDeleteFromRoom()
                val serverDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

                // Step 2: Handle additions and updates
                for (localNote in localNotesInitial) {
                    val correspondingServerNote = serverNotesInitial.find { it.id == localNote.serverId }

                    if (localNote.serverId == 0) {
                        // New note, create on server
                        val createResponse = noteApiRepo.createNote(CreateNoteRequest(localNote.title, localNote.notes))
                        localNote.serverId = createResponse.data.id
                        noteRepo.updateNoteInRoom(localNote)
                    } else if (correspondingServerNote != null) {
                        // Compare timestamps
                        val serverDate = serverDateFormat.parse(correspondingServerNote.updatedAt)
                        when {
                            localNote.updatedAt?.compareTo(serverDate) == 1 -> {
                                // Local note is newer, update on server
                                noteApiRepo.updateNote(localNote.serverId, CreateNoteRequest(localNote.title, localNote.notes))
                            }
                            localNote.updatedAt?.compareTo(serverDate) == -1 -> {
                                // Server note is newer, update locally
                                localNote.title = correspondingServerNote.title
                                localNote.notes = correspondingServerNote.description
                                noteRepo.updateNoteInRoom(localNote)
                            }
                        }
                    }
                }

                // Step 3: Refetch all server notes to get an updated list
                val updatedServerNotes = noteApiRepo.getAllNotes().data ?: emptyList()

                // Step 4: Handle new notes from the server
                val updatedLocalNotes = noteRepo.getAllNotesWithSoftDeleteFromRoom()
                val localNoteServerIds = updatedLocalNotes.map { it.serverId }.toSet()
                val softDeletedIds = updatedLocalNotes.filter { it.isDeleted == 1 }.map { it.serverId }.toSet()

                for (serverNote in updatedServerNotes) {
                    if (!localNoteServerIds.contains(serverNote.id) && !softDeletedIds.contains(serverNote.id)) {
                        val newLocalNote = NoteModel(
                            id = 0,
                            title = serverNote.title,
                            notes = serverNote.description,
                            createdAt = serverDateFormat.parse(serverNote.createdAt),
                            updatedAt = serverDateFormat.parse(serverNote.updatedAt),
                            serverId = serverNote.id
                        )
                        noteRepo.insertNoteToRoom(newLocalNote)
                    }
                }

                // Step 5: Handle deletions
                val serverNoteIds = updatedServerNotes.map { it.id }.toSet()
                for (localNote in updatedLocalNotes) {
                    if (!serverNoteIds.contains(localNote.serverId) || localNote.isDeleted == 1) {
                        if (localNote.isDeleted == 1) {
                            noteApiRepo.deleteNote(localNote.serverId)
                        }
                        noteRepo.deleteNoteFromRoom(localNote)
                    }
                }

                uiState.value = HomeScreenState.Success
                Log.d("SYNC", "Sync completed")

            } catch (e: NoConnectivityException) {
                uiState.value = e.message?.let { HomeScreenState.Error(it) }
            } catch (e: Exception) {
                uiState.value = e.message?.let { HomeScreenState.Error(it) }
            }
        }

        getAllNotes()
    }

    fun printAllNotesAsJson(noteDao: NoteDao) {
        val notes = noteDao.getAllNotesWithSoftDelete()
        val jsonArray = JSONArray()

        for (note in notes) {
            val jsonObject = JSONObject().apply {
                put("id", note.id)
                put("title", note.title)
                put("notes", note.notes)
                put("createdAt", note.createdAt)
                put("updatedAt", note.updatedAt)
                put("serverId", note.serverId)
                put("isDeleted", note.isDeleted)

            }
            jsonArray.put(jsonObject)
        }

        println(jsonArray.toString())
    }
}

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    object Success : HomeScreenState()
    data class Error(val message: String) : HomeScreenState()
}