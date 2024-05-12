package com.example.weatherapp.models.forecast.dependencies

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)