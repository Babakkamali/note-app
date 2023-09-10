package com.babakkamali.khatnevesht.data.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.babakkamali.khatnevesht.data.models.NoteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM noteModel WHERE isDeleted = 0 ORDER BY id ASC")
    fun getAllNotes(): Flow<List<NoteModel>>

    @Query("SELECT * FROM noteModel WHERE isDeleted = 0 and id = :noteId")
    fun getNoteById(noteId: Int): Flow<NoteModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: NoteModel)

    @Update
    suspend fun updateNote(noteModel: NoteModel)

    @Delete
    suspend fun deleteNote(noteModel: NoteModel)

    @Query("UPDATE noteModel SET isDeleted = 1 WHERE id = :noteId")
    suspend fun softDeleteNote(noteId: Int)

    @Query("DELETE FROM noteModel")
    fun deleteAllNotes()

    @RawQuery
    fun vacuumDatabase(query: SupportSQLiteQuery): Int

}