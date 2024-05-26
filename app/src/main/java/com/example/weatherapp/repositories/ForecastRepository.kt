package com.example.weatherapp.repositories

import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.models.forecast.ForecastModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ForecastRepository {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun getCurrentForecast(city: String, callback: (ForecastModel?) -> Unit) {
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=${BuildConfig.API_KEY}&lang=pl&units=metric")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("ForecastRepository", "getCurrentForecast onFailure: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val json = it.string()
                        val weather = gson.fromJson(json, ForecastModel::class.java)
                        callback(weather)
                    }
                } else {
                    Log.i("ForecastRepository", "getCurrentForecast onResponse: ${response.message}")
                    callback(null)
                }
            }
        })
    }
}