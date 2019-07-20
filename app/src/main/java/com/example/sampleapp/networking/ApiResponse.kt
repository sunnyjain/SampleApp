package com.example.sampleapp.networking

import com.google.gson.JsonElement


class ApiResponse(val status: Status, var jsonElement: JsonElement?, var error: Throwable?) {

    companion object {
        fun loading() = ApiResponse(Status.LOADING, null, null)
        fun success(jsonElement: JsonElement?) = ApiResponse(Status.SUCCESS, jsonElement, null)
        fun error(error: Throwable?) = ApiResponse(Status.ERROR, null, error)
    }
}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR,
    COMPLETED
}