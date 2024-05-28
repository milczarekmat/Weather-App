package com.example.weatherapp.views.adapters

import CityPreferences
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.weatherapp.R

class CityAdapter(context: Context, private val cities: MutableList<String>) : ArrayAdapter<String>(context, R.layout.cities_list_item, cities) {
    private var selectedPosition = -1
    private val cityPreferences = CityPreferences(context)

    init {
        selectedPosition = cityPreferences.selectedCity
        if (cities.isEmpty()) {
            cities.addAll(cityPreferences.cityList)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.cities_list_item, parent, false)

        val cityName = view.findViewById<TextView>(R.id.city_name)
        val cityButton = view.findViewById<Button>(R.id.city_button)
        val deleteButton = view.findViewById<Button>(R.id.delete_button)

        cityName.text = cities[position]

        cityButton.visibility = if (position == selectedPosition) View.GONE else View.VISIBLE
        deleteButton.isEnabled = position != selectedPosition

        cityButton.setOnClickListener {
            selectedPosition = position
            cityPreferences.selectedCity = position
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