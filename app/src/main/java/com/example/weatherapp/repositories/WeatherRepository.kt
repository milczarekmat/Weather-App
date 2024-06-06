package com.example.weatherapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.MetricsNames
import com.example.weatherapp.common.Network
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.preferences.AppPreferences
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException

object WeatherRepository {
    private val client = OkHttpClient()
    private val gson = Gson()

    private var currentWeather: MutableLiveData<CurrentWeatherModel?>? = MutableLiveData<CurrentWeatherModel?>()

    fun getCurrentWeather(context: Context): CurrentWeatherModel?{
        if (!Network.isNetworkAvailable(context)) {
            val file = File(context.filesDir, "weather_data")
            if (file.exists()) {
                val weatherData = file.readText()

                return gson.fromJson(weatherData, CurrentWeatherModel::class.java)
            }
        }
        return currentWeather!!.value
    }

    fun fetchCurrentWeather(appPreferences: AppPreferences, city: String, callback: (CurrentWeatherModel?) -> Unit) {
        val units = MetricsNames.getMetricValue(appPreferences.selectedMetric)

        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=$city&appid=${BuildConfig.API_KEY}&lang=pl&units=$units")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("WeatherRepository", "getCurrentWeather onFailure: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val json = it.string()
                        val weather = gson.fromJson(json, CurrentWeatherModel::class.java)
                        currentWeather?.postValue(weather)
                        callback(weather)
                    }
                } else {
                    Log.i("WeatherRepository", "getCurrentWeather onResponse: ${response.message}")
                    callback(null)
                }
            }
        })
    }
}