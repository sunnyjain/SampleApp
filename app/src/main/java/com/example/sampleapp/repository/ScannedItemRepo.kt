package com.example.sampleapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sampleapp.db.ScansDao
import com.example.sampleapp.extensions.performOnBackOutOnMain
import com.example.sampleapp.extensions.success
import com.example.sampleapp.vo.ScannedItem
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScannedItemRepo @Inject constructor(
                      private val scansDao: ScansDao,
                      private val compositeDisposable: CompositeDisposable): ScannedItemDataContract.Repository {

    override val fetchScannedItem: PublishSubject<ScannedItem?> = PublishSubject.create()

    override fun fetchScannedItemFor(scannedItemString: String?) {
        if(scannedItemString == null) return
        compositeDisposable.add(scansDao.findByScannedItem(scannedItemString).performOnBackOutOnMain()
            .subscribe({
                Log.e("test", it.toString())
                fetchScannedItem.success(it)
            }, {
                //if any error occurs
                fetchScannedItem.success(null)
            }))
    }

    override fun loadScannedItems(): LiveData<MutableList<ScannedItem>> {
        return scansDao.retrieveAllScannedRecords()
    }

    override fun saveScannedRecord(scannedItem: ScannedItem) {
        compositeDisposable.add(
            Single.create<Long>{
                scansDao.insertScannedRecord(scannedItem)
            }.performOnBackOutOnMain().subscribe()
        )
    }

    override fun clear() {
        compositeDisposable.clear()
    }

}