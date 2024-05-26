package com.example.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.repositories.ForecastRepository
import com.example.weatherapp.repositories.WeatherRepository

class MainViewModel : ViewModel() {
    private val currentWeatherRepository = WeatherRepository()
    private val forecastRepository = ForecastRepository()

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    private val _forecast = MutableLiveData<ForecastModel?>()

    val currentWeather: LiveData<CurrentWeatherModel?> get() = _currentWeather
    val forecast: LiveData<ForecastModel?> get() = _forecast

    fun getCurrentWeather(currentCity: String) {
        currentWeatherRepository.getCurrentWeather(currentCity) { weather ->
            _currentWeather.postValue(weather)
        }
    }

    fun getForecast(currentCity: String) {
        forecastRepository.getCurrentForecast(currentCity) { forecast ->
            _forecast.postValue(forecast)
        }
    }
}