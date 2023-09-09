package com.babakkamali.khatnevesht.data.models.apiCall

import com.google.gson.annotations.SerializedName

data class Note(
    val id: Int,
    @SerializedName("note_title") val noteTitle: String,
    @SerializedName("note_description") val noteDescription: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class CreateNoteRequest(
    @SerializedName("note_title") val noteTitle: String,
    @SerializedName("note_description") val noteDescription: String
)


data class CreateNoteResponse(
    @SerializedName("note_title") val noteTitle: String,
    @SerializedName("note_description") val noteDescription: String
)

data class GeneralResponse(
    val status: String,
    val message: String
)

data class SingleNoteResponse(
    val status: String,
    val message: String,
    val data: Note
)

data class AllNotesResponse(
    val data: List<Note>
)