package com.babakkamali.khatnevesht.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.babakkamali.khatnevesht.utils.DateConverter
import java.util.Date

@Entity(tableName = "noteModel")
@TypeConverters(DateConverter::class)
data class NoteModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title:String,
    var notes:String,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var serverId: Int = 0,
    var isDeleted: Int = 0
)