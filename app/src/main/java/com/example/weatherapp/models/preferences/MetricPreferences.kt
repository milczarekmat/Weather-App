package com.example.weatherapp.models.preferences

import android.content.Context
import android.content.SharedPreferences

class MetricPreferences(context: Context) {
    private val PREFS_NAME = "com.example.weatherapp.metricprefs"
    private val SELECTED_METRIC = "selected_metric"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var selectedMetric: String?
        get() = prefs.getString(SELECTED_METRIC, "Metryczna")
        set(value) = prefs.edit().putString(SELECTED_METRIC, value).apply()
}