package com.example.weatherapp.models.forecast.dependencies

import com.google.gson.annotations.Expose

data class Weather(
    @Expose val description: String,
    @Expose val icon: String,
    @Expose val id: Int,
    @Expose val main: String
)