package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.common.IconMap
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.viewmodels.MainViewModel

class CurrentWeatherFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().viewModels<MainViewModel>().value

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->

            if (currentWeather == null) {
                Log.i("CurrentWeatherFragment", "Weather is null")
                return@Observer
            }

            updateWeatherView(currentWeather)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }

    private fun updateWeatherView(weather: CurrentWeatherModel) {
        view?.findViewById<TextView>(R.id.titleTV)?.text = weather.name
        view?.findViewById<TextView>(R.id.subtitleTV)?.text = weather.weather[0].description
        view?.findViewById<TextView>(R.id.temperatureTV)?.text =
            weather.main.temp.toInt().toString() + "°C"
        val iconName = weather.weather[0].icon
        val iconResId = IconMap.map[iconName]
        if (iconResId != null)
            view?.findViewById<ImageView>(R.id.currentWeatherIV)?.setImageResource(iconResId)


        view?.findViewById<TextView>(R.id.pressureValueTV)?.text =
            weather.main.pressure.toString() + "hPa"
        view?.findViewById<TextView>(R.id.perceptibleTemperatureValueTV)?.text =
            weather.main.feels_like.toInt().toString() + "°C"
        view?.findViewById<TextView>(R.id.coordinatesValueLatTV)?.text =
            weather.coord.lat.toString()
        view?.findViewById<TextView>(R.id.coordinatesValueLonTV)?.text =
            weather.coord.lon.toString()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCurrentWeatherAndPostValue()
    }
}