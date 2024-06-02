package com.example.weatherapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.common.Network.isNetworkAvailable
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.models.preferences.AppPreferences
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.repositories.ForecastRepository
import com.example.weatherapp.repositories.WeatherRepository
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class MainViewModel(
    private val cityPreferences: CityPreferences,
    private val appPreferences: AppPreferences
) : ViewModel() {
    val currentWeather = MutableLiveData<CurrentWeatherModel?>()
    val forecast = MutableLiveData<ForecastModel?>()
    val updateTime = MutableLiveData<String>()
    private val gson = Gson()

    fun updateWeatherData() {
        val newCurrentWeather = WeatherRepository.getCurrentWeather()
        if (newCurrentWeather != currentWeather.value) {
            currentWeather.postValue(newCurrentWeather)
            postUpdateTimeInformation()
        }
    }

    fun updateForecastData() {
        forecast.postValue(ForecastRepository.getCurrentForecast())
    }

    fun getCurrentWeatherAndPostValue(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            WeatherRepository.fetchCurrentWeather(appPreferences, currentCity) { weather ->
                if (weather != null) {
                    val file = File(context.filesDir, "weather_data")
                    val weatherJson = gson.toJson(weather)
                    file.writeText(weatherJson)
                    postUpdateTimeInformation()

                    updateWeatherData()
                }
            }
        } else {
            val file = File(context.filesDir, "weather_data")
            if (file.exists()) {
                val weatherData = file.readText()

                val weatherModel = gson.fromJson(weatherData, CurrentWeatherModel::class.java)
                currentWeather.postValue(weatherModel)
            }
        }
    }

    fun getCurrentWeather(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            WeatherRepository.fetchCurrentWeather(appPreferences, currentCity) { weather ->
                if (weather != null) {
                    val file = File(context.filesDir, "weather_data")
                    val weatherJson = gson.toJson(weather)
                    file.writeText(weatherJson)
                    postUpdateTimeInformation()
                }
            }
        } else {
            val file = File(context.filesDir, "weather_data")
            if (file.exists()) {
                val weatherData = file.readText()

                val weatherModel = gson.fromJson(weatherData, CurrentWeatherModel::class.java)
                currentWeather.postValue(weatherModel)
            }
        }
    }

    fun getForecastAndPostValue(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            ForecastRepository.fetchCurrentForecast(appPreferences, currentCity) { newForecast ->
                val file = File(context.filesDir, "forecast_data")
                val forecastJson = gson.toJson(forecast)

                file.writeText(forecastJson)
                postUpdateTimeInformation()

                updateForecastData()
            }
        } else {
            val file = File(context.filesDir, "forecast_data")
            if (file.exists()) {
                val forecastData = file.readText()

                val forecastModel = gson.fromJson(forecastData, ForecastModel::class.java)
                forecast.postValue(forecastModel)
            }
        }
    }

    fun getForecast(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            ForecastRepository.fetchCurrentForecast(appPreferences, currentCity) { newForecast ->
                val file = File(context.filesDir, "forecast_data")
                val forecastJson = gson.toJson(forecast)

                file.writeText(forecastJson)
                postUpdateTimeInformation()
            }
        } else {
            val file = File(context.filesDir, "forecast_data")
            if (file.exists()) {
                val forecastData = file.readText()

                val forecastModel = gson.fromJson(forecastData, ForecastModel::class.java)
                forecast.postValue(forecastModel)
            }
        }
    }

    fun postUpdateTimeInformation() {
        val current = Calendar.getInstance()
        val timeZone = TimeZone.getTimeZone("Europe/Warsaw")
        current.timeZone = timeZone

        val formatter = SimpleDateFormat("HH:mm:ss", Locale("pl", "PL"))
        formatter.timeZone = timeZone
        val formatted = formatter.format(current.time)
        updateTime.postValue(formatted)

        appPreferences.lastUpdateTime = formatted
    }
}