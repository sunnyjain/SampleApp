package com.example.sampleapp.networking

import com.example.sampleapp.utils.Constants
import com.example.sampleapp.vo.ScannedItem
import com.google.gson.JsonElement
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiCallInterface {

    @FormUrlEncoded
    @POST(Constants.MOCK_CALL)
    fun sendDirtyRecords(@Field("scannedItems") scannedItems: List<ScannedItem>): Observable<JsonElement>
}
