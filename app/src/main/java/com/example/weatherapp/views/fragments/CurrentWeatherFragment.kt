package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.R
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

            view.findViewById<TextView>(R.id.titleTV).text = currentWeather.name
            view.findViewById<TextView>(R.id.subtitleTV).text = currentWeather.weather[0].description
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }
}