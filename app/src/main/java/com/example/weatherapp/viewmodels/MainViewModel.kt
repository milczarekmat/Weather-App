package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel

class MainViewModel : ViewModel() {
    var currentWeather: CurrentWeatherModel? = null
    var forecast: ForecastModel? = null
}