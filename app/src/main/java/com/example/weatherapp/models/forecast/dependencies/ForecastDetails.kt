package com.example.weatherapp.models.forecast.dependencies

import com.google.gson.annotations.Expose

data class ForecastDetails(
    @Expose val clouds: Clouds,
    @Expose val dt: Int,
    @Expose val dt_txt: String,
    @Expose val main: Main,
    @Expose val pop: Double,
    @Expose val sys: Sys,
    @Expose val visibility: Int,
    @Expose val weather: List<Weather>,
    @Expose val wind: Wind,
)