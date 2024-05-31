package com.example.weatherapp.common

object Units {
    private val unitsMap = mapOf(
        "metric" to "°C",
        "imperial" to "°F",
        "standard" to "K"
    )

    private val windSpeedUnitsMap = mapOf(
        "metric" to "m/s",
        "imperial" to "mph",
        "standard" to "m/s"
    )

    fun getTemperatureUnit(metric: String?): String {
        return unitsMap[metric] ?: "°C"
    }

    fun getWindSpeedUnit(metric: String?): String {
        return windSpeedUnitsMap[metric] ?: "m/s"
    }
}