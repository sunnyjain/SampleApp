package com.example.sampleapp.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.extensions.toLiveData
import com.example.sampleapp.repository.ScannedItemRepo
import com.example.sampleapp.vo.ScannedItem
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class ScannerPageViewModel
    @Inject constructor(private val scannedItemRepo: ScannedItemRepo):  ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    val scannedItemLiveData: LiveData<ScannedItem?> by lazy {
        scannedItemRepo.fetchScannedItem.toLiveData(compositeDisposable)
    }

    var scannedItem: String = ""

    fun addRecord() {
        scannedItemRepo.saveScannedRecord(ScannedItem(0, scannedItem, Date(), true))
    }

    fun getRecordByScannedItem(scannedItem: String) {
        scannedItemRepo.fetchScannedItemFor(scannedItem)
    }

    override fun onCleared() {
        super.onCleared()
        scannedItemRepo.clear()
        compositeDisposable.clear()
    }
}