package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

const val API_KEY = "3abe18f49cd30b5b92578b8572135931"

class MainViewModel : ViewModel() {
    private val client = OkHttpClient()

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    val currentWeather: LiveData<CurrentWeatherModel?> get() = _currentWeather
//    var currentWeather: CurrentWeatherModel? = null
//    var forecast: ForecastModel? = null

    val cities = ArrayList<String>()
    var currentCity = "Londyn"


    fun getCurrentWeather() {
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=$currentCity&appid=$API_KEY&lang=pl")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("MainViewModel", "getCurrentWeather onFailure: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val json = it.string()

                        _currentWeather.postValue(Gson().fromJson(json, CurrentWeatherModel::class.java))

                    }
                } else {
                    Log.i("MainViewModel", "getCurrentWeather onResponse: ${response.message}")
                }
            }

        })
    }
}