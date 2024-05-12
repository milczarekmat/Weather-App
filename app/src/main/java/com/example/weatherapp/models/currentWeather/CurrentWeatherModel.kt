package com.example.weatherapp.models.currentWeather

import com.example.weatherapp.models.currentWeather.dependencies.Clouds
import com.example.weatherapp.models.currentWeather.dependencies.Coord
import com.example.weatherapp.models.currentWeather.dependencies.Main
import com.example.weatherapp.models.currentWeather.dependencies.Sys
import com.example.weatherapp.models.currentWeather.dependencies.Weather
import com.example.weatherapp.models.currentWeather.dependencies.Wind

data class CurrentWeatherModel(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)