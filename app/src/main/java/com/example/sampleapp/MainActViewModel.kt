package com.example.sampleapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.R.attr.password
import com.example.sampleapp.extensions.performOnBackOutOnMain
import com.example.sampleapp.networking.ApiResponse
import com.example.sampleapp.repository.ScannedItemRepo
import com.example.sampleapp.vo.ScannedItem
import javax.inject.Inject


class MainActViewModel
    @Inject constructor(private val scannedItemRepo: ScannedItemRepo): ViewModel() {

    /*live data to basically to return the Api response on successful call*/
    val responseLiveData = MutableLiveData<ApiResponse>()

    //disposable.
    private val compositeDisposable = CompositeDisposable()

    /*updates the all the dirty records.*/
    fun updateDirtyRecords() {
        scannedItemRepo.updateDirtyRecords()
    }

    /*api call to send all the dirty records*/
    fun sendDirtyRecords() {
        scannedItemRepo.getRecordsToSync()
        compositeDisposable.add(scannedItemRepo.fetchScannedItems.subscribe {
            scannedItemRepo.syncRecords(it).performOnBackOutOnMain()
                .doOnSubscribe {
                    responseLiveData.value = ApiResponse.loading()
                }
                .subscribe ({
                    result -> responseLiveData.value = ApiResponse.success(result)
                }, {
                        result -> responseLiveData.value = ApiResponse.error(result)
                    })
        })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}