package com.example.weatherapp.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.common.Network.isNetworkAvailable
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.models.preferences.MetricPreferences
import com.example.weatherapp.repositories.ForecastRepository
import com.example.weatherapp.repositories.WeatherRepository
import com.google.gson.Gson
import java.io.File

class MainViewModel(private val cityPreferences: CityPreferences, metricPreferences: MetricPreferences) : ViewModel() {
    private val currentWeatherRepository = WeatherRepository(metricPreferences)
    private val forecastRepository = ForecastRepository(metricPreferences)

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    private val _forecast = MutableLiveData<ForecastModel?>()
    private val gson = Gson()

    val currentWeather: LiveData<CurrentWeatherModel?> get() = _currentWeather
    val forecast: LiveData<ForecastModel?> get() = _forecast

    fun getCurrentWeatherAndPostValue(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            currentWeatherRepository.fetchCurrentWeather(currentCity) { weather ->
                _currentWeather.postValue(weather)

                val file = File(context.filesDir, "weather_data")
                val weatherJson = gson.toJson(weather)

                file.writeText(weatherJson)
            }
        } else {
            val file = File(context.filesDir, "weather_data")
            if (file.exists()) {
                val weatherData = file.readText()

                val weatherModel = gson.fromJson(weatherData, CurrentWeatherModel::class.java)
                _currentWeather.postValue(weatherModel)
            }
        }
    }

    fun getForecastAndPostValue(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            forecastRepository.getCurrentForecast(currentCity) { forecast ->
                _forecast.postValue(forecast)

                val file = File(context.filesDir, "forecast_data")
                val forecastJson = gson.toJson(forecast)

                file.writeText(forecastJson)
            }
        } else {
            val file = File(context.filesDir, "forecast_data")
            if (file.exists()) {
                val forecastData = file.readText()

                val forecastModel = gson.fromJson(forecastData, ForecastModel::class.java)
                _forecast.postValue(forecastModel)
            }
        }
    }
}