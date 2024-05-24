package com.json.hatchworks_test.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.formatDate(): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

    val date: Date? = inputFormat.parse(this)
    return date?.let { outputFormat.format(it) } ?: ""
}

fun String.convertDateStringToReadable(): Date {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    return inputFormat.parse(this) ?: Date()
}