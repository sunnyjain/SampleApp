package com.example.sampleapp.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class ScannedItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var ScannedString: String,
    var timeStamp: Date,
    var isDirty: Boolean
    )