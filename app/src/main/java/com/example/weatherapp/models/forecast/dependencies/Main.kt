package com.example.weatherapp.models.forecast.dependencies

import com.google.gson.annotations.Expose

data class Main(
    @Expose val feels_like: Double,
    @Expose val grnd_level: Int,
    @Expose val humidity: Int,
    @Expose val pressure: Int,
    @Expose val sea_level: Int,
    @Expose val temp: Double,
    @Expose val temp_kf: Double,
    @Expose val temp_max: Double,
    @Expose val temp_min: Double
)