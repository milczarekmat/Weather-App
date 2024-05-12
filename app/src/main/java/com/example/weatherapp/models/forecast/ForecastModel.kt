package com.example.weatherapp.models.forecast

import com.example.weatherapp.models.forecast.dependencies.City
import com.example.weatherapp.models.forecast.dependencies.ForecastDetails

data class ForecastModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastDetails>,
    val message: Int
)