package com.example.sampleapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sampleapp.db.ScansDao
import com.example.sampleapp.extensions.performOnBackOutOnMain
import com.example.sampleapp.extensions.success
import com.example.sampleapp.networking.ApiCallInterface
import com.example.sampleapp.vo.ScannedItem
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScannedItemRepo @Inject constructor(
                      private val scansDao: ScansDao,
                      private val apiCallInterface: ApiCallInterface,
                      private val compositeDisposable: CompositeDisposable): ScannedItemDataContract.Repository {


    override val fetchScannedItem: PublishSubject<Int> = PublishSubject.create()
    override val fetchScannedItems: PublishSubject<List<ScannedItem>> = PublishSubject.create()

    override fun fetchScannedItemFor(scannedItemString: String?){
        if(scannedItemString == null) return
        compositeDisposable.add(scansDao.findByScannedItem(scannedItemString).performOnBackOutOnMain()
            .subscribe({
                Log.e("test", it.toString())
                fetchScannedItem.success(it)
            }, {
                //if any error occurs
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

    override fun getRecordsToSync() {
        compositeDisposable.add(scansDao.retrieveDirtyRecords().performOnBackOutOnMain()
            .subscribe({
                fetchScannedItems.success(it)
            }, {

            }))
    }

    override fun syncRecords(scannedItems: List<ScannedItem>)
         = apiCallInterface.sendDirtyRecords(scannedItems)

    override fun updateDirtyRecords() {
        compositeDisposable.add(scansDao.retrieveDirtyRecords().performOnBackOutOnMain()
            .subscribe({
                records -> records.forEach { if(it.isDirty) it.isDirty = false }
                Single.create<Int> {
                    scansDao.udpateDirtyRecords(records)
                }.performOnBackOutOnMain().subscribe()
            }, {

            }))

    }

    override fun clear() {
        compositeDisposable.clear()
    }

}