package com.example.sampleapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sampleapp.vo.ScannedItem
import util.DateConvertor


@Database(
    entities = [
        ScannedItem::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConvertor::class)
abstract class Scans: RoomDatabase() {
    abstract fun scansDao(): ScansDao
}