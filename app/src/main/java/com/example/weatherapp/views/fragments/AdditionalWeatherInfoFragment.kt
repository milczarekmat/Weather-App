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
import com.example.weatherapp.common.DateConverter.Companion.convertUnixToTime
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.viewmodels.MainViewModel

class AdditionalWeatherInfoFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        view?.findViewById<TextView>(R.id.sunriseValueTV)?.text = convertUnixToTime(payload.sys.sunrise + payload.timezone)
        view?.findViewById<TextView>(R.id.sunsetValueTV)?.text = convertUnixToTime(payload.sys.sunset + payload.timezone)

        view?.findViewById<TextView>(R.id.visibilityValueTV)?.text = payload.visibility.toString() + "m"
        view?.findViewById<TextView>(R.id.windPowerValueTV)?.text =
            payload.wind.speed.toString() + "m/s"

        view?.findViewById<TextView>(R.id.cloudPerValueTV)?.text = payload.clouds.all.toString() + "%"
        view?.findViewById<TextView>(R.id.maxTempValueTV)?.text = payload.main.temp_max.toInt().toString() + "°C"

        view?.findViewById<TextView>(R.id.humidityValueTV)?.text = payload.main.humidity.toString() + "%"
        view?.findViewById<TextView>(R.id.minTempValueTV)?.text = payload.main.temp_min.toInt().toString() + "°C"
    }


}