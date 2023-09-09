package com.babakkamali.khatnevesht.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noteModel")
data class NoteModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
//    var serverId: Int,
    var title:String,
    var notes:String
)