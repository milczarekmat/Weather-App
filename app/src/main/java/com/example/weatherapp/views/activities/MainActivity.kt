package com.example.weatherapp.views.activities

import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

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

        viewModel.getCurrentWeather("Lodz")
    }
}