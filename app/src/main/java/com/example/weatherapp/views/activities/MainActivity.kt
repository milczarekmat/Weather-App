package com.example.weatherapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.models.preferences.MetricPreferences
import com.example.weatherapp.viewmodels.MainViewModel
import com.example.weatherapp.viewmodels.factories.MainViewModelFactory
import com.example.weatherapp.views.adapters.ViewPagerAdapter
import com.example.weatherapp.views.fragments.AdditionalWeatherInfoFragment
import com.example.weatherapp.views.fragments.CurrentWeatherFragment
import com.example.weatherapp.views.fragments.ForecastFragment

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var metricPreferences: MetricPreferences
    private val metrics = arrayOf<String?>("Metryczna", "Imperialna", "Kelwiny dla temp. i m/s dla wiatru")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val fragmentList = arrayListOf(
            CurrentWeatherFragment(),
            AdditionalWeatherInfoFragment(),
            ForecastFragment()
        )

        val adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)
        val viewPager = findViewById<ViewPager2>(R.id.mainVP)
        viewPager.adapter = adapter

        val cityPreferences = CityPreferences(this)
        val metricsPreferences = MetricPreferences(this)
        viewModel = ViewModelProvider(this, MainViewModelFactory(cityPreferences, metricsPreferences)).get(MainViewModel::class.java)

        metricPreferences = MetricPreferences(this)

        viewModel.getCurrentWeatherAndPostValue()
        viewModel.getForecastAndPostValue()

        setSpinner()
        setUpListeners()
    }

    private fun setUpListeners() {
//        val spinner = findViewById<Spinner>(R.id.metricSpinner)
//        spinner.onItemSelectedListener = this

        val locationSettingsBtn = findViewById<Button>(R.id.locationSettingsBtn)

        locationSettingsBtn.setOnClickListener {
            startActivity(Intent(this, LocationSettingsActivity::class.java))
        }
    }

    private fun setSpinner() {
        val spinner = findViewById<Spinner>(R.id.metricSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, metrics)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val selectedMetric = metricPreferences.selectedMetric
        val selectedIndex = metrics.indexOf(selectedMetric)
        spinner.setSelection(selectedIndex)
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedMetric = metrics[position]
        metricPreferences.selectedMetric = selectedMetric
        viewModel.getCurrentWeatherAndPostValue()
        viewModel.getForecastAndPostValue()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nie wybrano żadnej opcji", Toast.LENGTH_SHORT).show()
    }
}