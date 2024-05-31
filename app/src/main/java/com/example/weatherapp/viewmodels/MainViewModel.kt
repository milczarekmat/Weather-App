package com.example.weatherapp.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.common.Network.isNetworkAvailable
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.models.preferences.AppPreferences
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
    private val currentWeatherRepository = WeatherRepository(appPreferences)
    private val forecastRepository = ForecastRepository(appPreferences)

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    private val _forecast = MutableLiveData<ForecastModel?>()
    val updateTime = MutableLiveData<String>()
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

                postUpdateTimeInformation()
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

                postUpdateTimeInformation()
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

    private fun postUpdateTimeInformation() {
        val current = Calendar.getInstance()
        val timeZone = TimeZone.getTimeZone("Europe/Warsaw")
        current.timeZone = timeZone

        val formatter = SimpleDateFormat("HH:mm", Locale("pl", "PL"))
        formatter.timeZone = timeZone
        val formatted = formatter.format(current.time)
        updateTime.postValue(formatted)

        appPreferences.lastUpdateTime = formatted
    }
}