package com.json.hatchworks_test.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context) {
    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}