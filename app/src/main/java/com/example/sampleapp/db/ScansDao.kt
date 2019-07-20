package com.example.sampleapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.sampleapp.vo.ScannedItem
import io.reactivex.Maybe
import io.reactivex.Single


@Dao
interface ScansDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScannedRecord(record: ScannedItem): Long

    @Query("SELECT * FROM ScannedItem")
    fun retrieveAllScannedRecords(): LiveData<MutableList<ScannedItem>>

    @Query("SELECT * FROM ScannedItem where isDirty = 1")
    fun retrieveDirtyRecords(): Single<List<ScannedItem>>

    @Update
    fun udpateDirtyRecords(scannedItems: List<ScannedItem>): Int


    @Query("SELECT COUNT(*) FROM ScannedItem where ScannedString = :scannedItem")
    fun findByScannedItem(scannedItem: String) : Maybe<Int>
}