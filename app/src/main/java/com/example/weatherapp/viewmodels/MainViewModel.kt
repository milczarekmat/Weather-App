package com.example.weatherapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.common.Network.isNetworkAvailable
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.models.preferences.AppPreferences
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.repositories.ForecastRepository
import com.example.weatherapp.repositories.WeatherRepository
import com.google.gson.GsonBuilder
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
    private val gsonWithExclusion = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    private val gsonWithoutExclusion = GsonBuilder().create()

    fun updateWeatherData(context: Context) {
        val newCurrentWeather = WeatherRepository.getCurrentWeather(context)
        if (newCurrentWeather != currentWeather.value) {
            currentWeather.postValue(newCurrentWeather)
            postUpdateTimeInformation()
        }
    }

    fun updateForecastData(context: Context) {
        val newCurrentForecast = ForecastRepository.getCurrentForecast(context)
        if (newCurrentForecast != forecast.value && newCurrentForecast != null) {
            forecast.postValue(newCurrentForecast)
            postUpdateTimeInformation()
        }
    }

    fun getCurrentWeatherAndPostValue(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            WeatherRepository.fetchCurrentWeather(appPreferences, currentCity) { weather ->
                if (weather != null) {
                    val file = File(context.filesDir, "weather_data")
                    val weatherJson = gsonWithoutExclusion.toJson(weather)

                    if (weatherJson.isNotEmpty()) {
                        file.writeText(weatherJson)
                        postUpdateTimeInformation()
                    } else {
                        Log.e("MainViewModel", "Failed to convert weather data to JSON")
                    }

                    currentWeather.postValue(weather)
                }
            }
        }
    }

    fun getCurrentWeather(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            WeatherRepository.fetchCurrentWeather(appPreferences, currentCity) { weather ->
                if (weather != null) {
                    val file = File(context.filesDir, "weather_data")
                    val weatherJson = gsonWithoutExclusion.toJson(weather)

                    if (weatherJson.isNotEmpty()) {
                        file.writeText(weatherJson)
                        postUpdateTimeInformation()
                    } else {
                        Log.e("MainViewModel", "Failed to convert weather data to JSON")
                    }

                }
            }
        }
    }

    fun getForecastAndPostValue(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            ForecastRepository.fetchCurrentForecast(appPreferences, currentCity) { newForecast ->
                if (newForecast == null) {
                    return@fetchCurrentForecast
                }

                val file = File(context.filesDir, "forecast_data")
                val forecastJson = gsonWithExclusion.toJson(newForecast)

                if (forecastJson.isNotEmpty()) {
                    file.writeText(forecastJson)
                    postUpdateTimeInformation()
                } else {
                    Log.e("MainViewModel", "Failed to convert forecast data to JSON")
                }

                forecast.postValue(newForecast)
            }
        }
    }

    fun getForecast(context: Context) {
        val currentCity = cityPreferences.cityList[cityPreferences.selectedCity]
        if (isNetworkAvailable(context)) {
            ForecastRepository.fetchCurrentForecast(appPreferences, currentCity) { newForecast ->
                if (newForecast == null) {
                    return@fetchCurrentForecast
                }

                val file = File(context.filesDir, "forecast_data")
                val forecastJson = gsonWithExclusion.toJson(newForecast)

                if (forecastJson.isNotEmpty()) {
                    file.writeText(forecastJson)
                    postUpdateTimeInformation()
                } else {
                    Log.e("MainViewModel", "Failed to convert forecast data to JSON")
                }
            }
        }
    }

    private fun postUpdateTimeInformation() {
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