package com.example.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.models.preferences.MetricPreferences
import com.example.weatherapp.repositories.ForecastRepository
import com.example.weatherapp.repositories.WeatherRepository

class MainViewModel(private val cityPreferences: CityPreferences, metricPreferences: MetricPreferences) : ViewModel() {
    private val currentWeatherRepository = WeatherRepository(metricPreferences)
    private val forecastRepository = ForecastRepository(metricPreferences)

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    private val _forecast = MutableLiveData<ForecastModel?>()

    val currentWeather: LiveData<CurrentWeatherModel?> get() = _currentWeather
    val forecast: LiveData<ForecastModel?> get() = _forecast

    fun getCurrentWeatherAndPostValue() {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        currentWeatherRepository.fetchCurrentWeather(currentCity) { weather ->
            _currentWeather.postValue(weather)
        }
    }

    fun getForecastAndPostValue() {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        forecastRepository.getCurrentForecast(currentCity) { forecast ->
            _forecast.postValue(forecast)
        }
    }
}