package com.babakkamali.khatnevesht.domain

import com.babakkamali.khatnevesht.data.api.ApiService
import com.babakkamali.khatnevesht.data.models.apiCall.CreateNoteRequest

class NoteApiRepository(private val apiService: ApiService) {

    suspend fun getAllNotes() = apiService.getAllNotes()

    suspend fun createNote(request: CreateNoteRequest) = apiService.createNote(request)

    suspend fun getNoteById(id: Int) = apiService.getNoteById(id)

    suspend fun updateNote(id: Int, request: CreateNoteRequest) = apiService.updateNote(id, request)

    suspend fun deleteNote(id: Int) = apiService.deleteNote(id)
}