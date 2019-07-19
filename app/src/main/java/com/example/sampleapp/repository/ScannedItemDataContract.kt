package com.example.sampleapp.repository

import androidx.lifecycle.LiveData
import com.example.sampleapp.vo.ScannedItem
import io.reactivex.subjects.PublishSubject

interface ScannedItemDataContract {
    interface Repository {
        val fetchScannedItem: PublishSubject<ScannedItem?>
        fun fetchScannedItemFor(scannedItemString: String?)
        fun loadScannedItems(): LiveData<MutableList<ScannedItem>>
        fun saveScannedRecord(scannedItem: ScannedItem)
        fun clear()
    }
}