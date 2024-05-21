package com.example.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.repositories.WeatherRepository

class MainViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    val currentWeather: LiveData<CurrentWeatherModel?> get() = _currentWeather

    fun getCurrentWeather(currentCity: String) {
        repository.getCurrentWeather(currentCity) { weather ->
            _currentWeather.postValue(weather)
        }
    }
}