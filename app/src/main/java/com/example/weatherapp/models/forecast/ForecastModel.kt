package com.example.weatherapp.models.forecast

import com.example.weatherapp.models.forecast.dependencies.ForecastDetails

data class ForecastModel(
    val cod: String,
    val cnt: Int,
    val message: Int,
    val list: List<ForecastDetails>,
)