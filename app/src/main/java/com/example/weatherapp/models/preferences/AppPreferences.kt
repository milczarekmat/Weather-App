package com.example.weatherapp.models.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val PREFS_NAME = "com.example.weatherapp.appprefs"
    private val SELECTED_METRIC = "selected_metric"
    private val LAST_UPDATE_TIME = "last_update_time"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var selectedMetric: String?
        get() = prefs.getString(SELECTED_METRIC, "Metryczna")
        set(value) = prefs.edit().putString(SELECTED_METRIC, value).apply()

    var lastUpdateTime: String?
        get() = prefs.getString(LAST_UPDATE_TIME, "")
        set(value) = prefs.edit().putString(LAST_UPDATE_TIME, value).apply()
}