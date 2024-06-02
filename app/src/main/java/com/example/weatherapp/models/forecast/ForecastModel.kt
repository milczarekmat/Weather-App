package com.example.weatherapp.models.forecast

import com.example.weatherapp.models.forecast.dependencies.ForecastDetails
import com.google.gson.annotations.Expose

data class ForecastModel(
    @Expose val cod: String,
    @Expose val cnt: Int,
    @Expose val message: Int,
    @Expose val list: List<ForecastDetails>,
)