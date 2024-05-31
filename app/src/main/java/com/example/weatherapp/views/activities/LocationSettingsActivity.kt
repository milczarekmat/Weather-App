package com.example.weatherapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.common.Network
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.models.preferences.AppPreferences
import com.example.weatherapp.repositories.WeatherRepository
import com.example.weatherapp.viewmodels.MainViewModel
import com.example.weatherapp.viewmodels.factories.MainViewModelFactory
import com.example.weatherapp.views.adapters.CityAdapter
import kotlinx.coroutines.launch

class LocationSettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var cityPreferences: CityPreferences
    private lateinit var cities: MutableList<String>
    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location_settings)

        cityPreferences = CityPreferences(this)
        cities = cityPreferences.cityList.toMutableList()

        val metricsPreferences = AppPreferences(this)
        weatherRepository = WeatherRepository(metricsPreferences)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(cityPreferences, metricsPreferences)
        ).get(MainViewModel::class.java)
        adapter = CityAdapter(this, cities)

        setUpListeners()

        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
    }

    private fun searchCity(cityName: String) {
        if (!Network.isNetworkAvailable(this)) {
            Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show()
            return
        }

        if(cities.contains(cityName.trim().lowercase().replaceFirstChar { it.uppercase() })){
            Toast.makeText(this, "Miasto już jest na liście", Toast.LENGTH_SHORT).show()
            return
        }

        weatherRepository.fetchCurrentWeather(cityName) { weather ->
            runOnUiThread {
                if (weather != null) {
                    addCity(cityName)
                } else {
                    Toast.makeText(this, "Nie znaleziono miasta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addCity(cityName: String) {
        cities.add(cityName.trim().lowercase().replaceFirstChar { it.uppercase() })
        cityPreferences.cityList = cities
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Dodano miasto do listy", Toast.LENGTH_SHORT).show()

        if (cityPreferences.cityList.size == 1) {
            val intent = Intent(this@LocationSettingsActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpListeners() {
        val backBtn = findViewById<Button>(R.id.backBtn)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val locationInput = findViewById<EditText>(R.id.location_input)

        backBtn.setOnClickListener {
            if (cityPreferences.cityList.isEmpty()) {
                Toast.makeText(this, "Musisz dodać przynajmniej jedno miasto", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }

        searchBtn.setOnClickListener {
            val cityName = locationInput.text.toString()
            if (cityName.isNotEmpty()) {
                lifecycleScope.launch {
                    searchCity(cityName)
                    locationInput.text.clear()
                }
            } else {
                Toast.makeText(this, "Wpisz nazwę miasta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}