package com.example.sampleapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.repository.ScannedItemRepo
import com.example.sampleapp.vo.ScannedItem
import javax.inject.Inject

class ScanHistoryViewModel
@Inject constructor(private val scannedItemRepo: ScannedItemRepo) : ViewModel() {
    val scannedItemList: LiveData<MutableList<ScannedItem>> = scannedItemRepo.loadScannedItems()
}