package com.example.weatherapp.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.MetricsNames
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.models.preferences.AppPreferences
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object ForecastRepository {
    private val client = OkHttpClient()
    private val gson = Gson()

    private var currentForecast: MutableLiveData<ForecastModel?>? = MutableLiveData<ForecastModel?>()

    fun getCurrentForecast(): ForecastModel?{
        return currentForecast!!.value
    }


    fun fetchCurrentForecast(appPreferences: AppPreferences, city: String, callback: (ForecastModel?) -> Unit) {
        val units = MetricsNames.getMetricValue(appPreferences.selectedMetric)

        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=${BuildConfig.API_KEY}&lang=pl&units=$units")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("ForecastRepository", "getCurrentWeather onFailure: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val json = it.string()
                        val forecast = gson.fromJson(json, ForecastModel::class.java)
                        currentForecast?.postValue(forecast)
                        callback(forecast)
                    }
                } else {
                    Log.i("ForecastRepository", "getCurrentWeather onResponse: ${response.message}")
                    callback(null)
                }
            }
        })
    }
}