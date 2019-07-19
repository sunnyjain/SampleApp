package com.example.sampleapp.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class ScannedItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val ScannedString: String,
    val timeStamp: Date,
    val isDirty: Boolean
    )