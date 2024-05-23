package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.models.currentWeather.CurrentWeatherModel
import com.example.weatherapp.viewmodels.MainViewModel
import kotlin.math.round

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

    val iconMap = mapOf(
        "01d" to R.drawable.i01d,
        "01n" to R.drawable.i01n,
        "02d" to R.drawable.i02d,
        "02n" to R.drawable.i02n,
        "03d" to R.drawable.i03d,
        "03n" to R.drawable.i03n,
        "04d" to R.drawable.i04d,
        "04n" to R.drawable.i04n,
        "09d" to R.drawable.i09d,
        "09n" to R.drawable.i09n,
        "10d" to R.drawable.i10d,
        "10n" to R.drawable.i10n,
        "11d" to R.drawable.i11d,
        "11n" to R.drawable.i11n,
        "13d" to R.drawable.i13d,
        "13n" to R.drawable.i13n,
        "50d" to R.drawable.i50d,
        "50n" to R.drawable.i50n
    )

    private fun updateWeatherView(weather: CurrentWeatherModel) {
        view?.findViewById<TextView>(R.id.titleTV)?.text = weather.name
        view?.findViewById<TextView>(R.id.subtitleTV)?.text = weather.weather[0].description
        view?.findViewById<TextView>(R.id.temperatureTV)?.text = weather.main.temp.toInt().toString() + "°C"
        val iconName = weather.weather[0].icon
        val iconResId = iconMap[iconName]
        if (iconResId != null)
            view?.findViewById<ImageView>(R.id.currentWeatherIV)?.setImageResource(iconResId)


        view?.findViewById<TextView>(R.id.cloudPerValueTV)?.text = weather.clouds.all.toString() + "%"
        view?.findViewById<TextView>(R.id.humidityValueTV)?.text = weather.main.humidity.toString() + "%"
        view?.findViewById<TextView>(R.id.visibilityValueTV)?.text = weather.visibility.toString() + "m"

        view?.findViewById<TextView>(R.id.pressureValueTV)?.text = weather.main.pressure.toString() + "hPa"
    }
}