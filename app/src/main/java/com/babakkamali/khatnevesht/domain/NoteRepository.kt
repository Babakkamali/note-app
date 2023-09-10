package com.babakkamali.khatnevesht.domain

import android.app.Application
import androidx.sqlite.db.SimpleSQLiteQuery
import com.babakkamali.khatnevesht.data.daos.NoteDao
import com.babakkamali.khatnevesht.data.database.NoteDatabase
import com.babakkamali.khatnevesht.data.models.NoteModel
import kotlinx.coroutines.flow.Flow

class NoteRepository(application: Application) {
    private var noteDao: NoteDao
    val vacuumQuery = SimpleSQLiteQuery("VACUUM")

    init {
        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
    }

    fun getAllNotesFromRoom(): Flow<List<NoteModel>> = noteDao.getAllNotes()
    fun getNoteByIdFromRoom(noteId: Int): Flow<NoteModel> = noteDao.getNoteById(noteId)
    suspend fun insertNoteToRoom(noteModel: NoteModel) = noteDao.insertNote(noteModel)
    suspend fun updateNoteInRoom(noteModel: NoteModel) = noteDao.updateNote(noteModel)
    suspend fun deleteNoteFromRoom(noteModel: NoteModel) = noteDao.deleteNote(noteModel)
    suspend fun softDeleteNoteFromRoom(noteId: Int) = noteDao.softDeleteNote(noteId)
    fun deleteAllNotes() { noteDao.deleteAllNotes() }
    fun vacuumDatabase() { noteDao.vacuumDatabase(vacuumQuery) }

    fun clearDatabase() {
        deleteAllNotes()
        vacuumDatabase()
    }
}