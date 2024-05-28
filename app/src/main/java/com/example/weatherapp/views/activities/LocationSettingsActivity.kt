package com.example.weatherapp.views.activities

import com.example.weatherapp.views.adapters.CityAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R

class LocationSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location_settings)

        setUpListeners()

        val cities = mutableListOf("City1", "City2", "City3")

        val adapter = CityAdapter(this, cities)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
    }

    private fun setUpListeners() {
        val backBtn = findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }
    }
}