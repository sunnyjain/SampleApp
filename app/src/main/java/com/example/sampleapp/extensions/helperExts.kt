package com.example.sampleapp.extensions

import android.content.Context
import com.example.sampleapp.R
import java.text.SimpleDateFormat
import java.util.*

fun Date.convertToReadableString(context: Context): String {
    val formatter = SimpleDateFormat(context.getString(R.string.def_date_format))
    return formatter.format(this).toString()
}