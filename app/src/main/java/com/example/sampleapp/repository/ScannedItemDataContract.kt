package com.example.sampleapp.repository

import androidx.lifecycle.LiveData
import com.example.sampleapp.vo.ScannedItem
import com.google.gson.JsonElement
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface ScannedItemDataContract {
    interface Repository {
        val fetchScannedItem: PublishSubject<Int>
        val fetchScannedItems: PublishSubject<List<ScannedItem>>
        fun fetchScannedItemFor(scannedItemString: String?)
        fun loadScannedItems(): LiveData<MutableList<ScannedItem>>
        fun saveScannedRecord(scannedItem: ScannedItem)
        fun syncRecords(scannedItems: List<ScannedItem>): Observable<JsonElement>
        fun getRecordsToSync()
        fun updateDirtyRecords()
        fun clear()
    }
}