package com.example.weatherapp.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.common.Network
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.viewmodels.MainViewModel

class CityAdapter(
    context: Context,
    private val cities: MutableList<String>,
    private val viewModel: MainViewModel
) : ArrayAdapter<String>(context, R.layout.cities_list_item, cities) {
    private var selectedPosition = -1
    private val cityPreferences = CityPreferences(context)

    init {
        selectedPosition = cityPreferences.selectedCity
        if (cities.isEmpty()) {
            cities.addAll(cityPreferences.cityList)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.cities_list_item, parent, false)

        val cityName = view.findViewById<TextView>(R.id.city_name)
        val selectBtn = view.findViewById<Button>(R.id.selectCityBtn)
        val deleteButton = view.findViewById<Button>(R.id.delete_button)

        cityName.text = cities[position]

        selectBtn.visibility = if (position == selectedPosition) View.GONE else View.VISIBLE
        deleteButton.isEnabled = position != selectedPosition

        selectBtn.setOnClickListener {
            if (!Network.isNetworkAvailable(context)) {
                Toast.makeText(context, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            selectedPosition = position
            cityPreferences.selectedCity = position

            viewModel.getCurrentWeather(context)
            viewModel.getForecast(context)

            notifyDataSetChanged()
        }

        deleteButton.setOnClickListener {
            if (position != selectedPosition) {
                cities.removeAt(position)
                cityPreferences.cityList = cities
                if (selectedPosition > position) {
                    selectedPosition--
                    cityPreferences.selectedCity = selectedPosition
                }
                notifyDataSetChanged()
            }
        }

        return view
    }
}