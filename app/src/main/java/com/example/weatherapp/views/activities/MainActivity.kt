package com.example.weatherapp.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.viewmodels.MainViewModel
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.views.adapters.ViewPagerAdapter
import com.example.weatherapp.views.fragments.AdditionalWeatherInfoFragment
import com.example.weatherapp.views.fragments.CurrentWeatherFragment
import com.example.weatherapp.views.fragments.ForecastFragment

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val viewModel: MainViewModel by viewModels()
    private val metrics = arrayOf<String?>("Metric", "Imperial", "Standard")
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

        viewModel.getCurrentWeather("Londyn")

        setSpinner()
    }

    private fun setSpinner() {
        val spinner = findViewById<Spinner>(R.id.metricSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, metrics)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(
            applicationContext,
            metrics[position],
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}