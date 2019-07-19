package com.example.sampleapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sampleapp.vo.ScannedItem
import io.reactivex.Single


@Dao
interface ScansDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScannedRecord(record: ScannedItem): Long

    @Query("SELECT * FROM ScannedItem")
    fun retrieveAllScannedRecords(): LiveData<MutableList<ScannedItem>>

    @Query("SELECT * FROM ScannedItem where id = :id")
    fun findById(id: Long) : Single<ScannedItem>
}