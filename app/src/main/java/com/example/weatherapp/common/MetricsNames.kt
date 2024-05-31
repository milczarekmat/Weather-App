package com.example.weatherapp.common

object MetricsNames {
    private val map = mapOf(
        "Metryczna" to "metric",
        "Imperialna" to "imperial",
        "Kelwiny dla temp. i m/s dla wiatru" to "standard"
    )

    fun getMetricValue(name: String?): String {
        return map[name] ?: "metric"
    }
}