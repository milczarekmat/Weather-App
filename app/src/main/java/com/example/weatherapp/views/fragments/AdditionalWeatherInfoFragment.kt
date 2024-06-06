package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.common.DateConverter.convertUnixToTime
import com.example.weatherapp.common.Units
import com.example.weatherapp.common.MetricsNames
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.models.preferences.AppPreferences
import com.example.weatherapp.viewmodels.MainViewModel

class AdditionalWeatherInfoFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreferences = AppPreferences(requireContext())
    }

    override fun onStart() {
        super.onStart()
        viewModel = requireActivity().viewModels<MainViewModel>().value

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weatherInfo ->

            if (weatherInfo == null) {
                Log.i("CurrentWeatherFragment", "Weather is null")
                return@Observer
            }

            updateAdditionalInfoView(weatherInfo)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_additional_weather_info, container, false)
    }

    private fun updateAdditionalInfoView(payload: CurrentWeatherModel) {
        val temperatureUnit = Units.getTemperatureUnit(MetricsNames.getMetricValue(appPreferences.selectedMetric))
        val windUnit = Units.getWindSpeedUnit(MetricsNames.getMetricValue(appPreferences.selectedMetric))

        view?.findViewById<TextView>(R.id.sunriseValueTV)?.text =
            convertUnixToTime(payload.sys.sunrise + payload.timezone )
        view?.findViewById<TextView>(R.id.sunsetValueTV )?.text =
            convertUnixToTime(payload.sys.sunset + payload.timezone)

        view?.findViewById<TextView>(R.id.visibilityValueTV)?.text =
            payload.visibility.toString() + "m"
        view?.findViewById<TextView>(R.id.windPowerValueTV)?.text =
            payload.wind.speed.toString() + windUnit

        view?.findViewById<TextView>(R.id.cloudPerValueTV)?.text =
            payload.clouds.all.toString() + "%"
        view?.findViewById<TextView>(R.id.maxTempValueTV)?.text =
            payload.main.temp_max.toInt().toString() + temperatureUnit

        view?.findViewById<TextView>(R.id.humidityValueTV)?.text =
            payload.main.humidity.toString() + "%"
        view?.findViewById<TextView>(R.id.minTempValueTV)?.text =
            payload.main.temp_min.toInt().toString() + temperatureUnit
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateWeatherData()
    }
}