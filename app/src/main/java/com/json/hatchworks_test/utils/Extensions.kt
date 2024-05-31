package com.json.hatchworks_test.utils

import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun String.formatDate(): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

    val date: Date? = inputFormat.parse(this)
    return date?.let { outputFormat.format(it) } ?: ""
}

fun String.convertDateStringToReadable(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
    val date: Date = inputFormat.parse(this) ?: Date()
    val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
    return outputFormat.format(date)
}