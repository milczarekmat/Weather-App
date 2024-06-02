package com.example.weatherapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.common.Network
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.models.preferences.AppPreferences
import com.example.weatherapp.viewmodels.MainViewModel
import com.example.weatherapp.viewmodels.factories.MainViewModelFactory
import com.example.weatherapp.views.adapters.ViewPagerAdapter
import com.example.weatherapp.views.fragments.AdditionalWeatherInfoFragment
import com.example.weatherapp.views.fragments.CurrentWeatherFragment
import com.example.weatherapp.views.fragments.ForecastFragment
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var appPreferences: AppPreferences
    private val metrics =
        arrayOf<String?>("Metryczna", "Imperialna", "Kelwiny dla temp. i m/s dla wiatru")

    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            if (!Network.isNetworkAvailable(this@MainActivity)) {
                return
            }
            viewModel.getCurrentWeatherAndPostValue(this@MainActivity)
            viewModel.getForecastAndPostValue(this@MainActivity)
            Toast.makeText(
                this@MainActivity,
                "Automatycznie zaktualizowano dane",
                Toast.LENGTH_SHORT
            ).show()
            handler.postDelayed(this, 5000) // 60000 ms = 1 minuta
        }
    }

    private fun restartTimer() {
        handler.removeCallbacks(refreshRunnable)
        handler.post(refreshRunnable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        handler.post(refreshRunnable)

        val fragmentList = arrayListOf(
            CurrentWeatherFragment(),
            AdditionalWeatherInfoFragment(),
            ForecastFragment()
        )

        val adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)

        val viewPager = findViewById<ViewPager2>(R.id.mainVP)
        viewPager?.let {
            it.adapter = adapter
        }

        val cityPreferences = CityPreferences(this)

        if (cityPreferences.cityList.isEmpty()) {
            startActivity(Intent(this, LocationSettingsActivity::class.java))
            finish()
            return
        }

        val metricsPreferences = AppPreferences(this)
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(cityPreferences, metricsPreferences)).get(
                MainViewModel::class.java
            )

        appPreferences = AppPreferences(this)

        viewModel.getCurrentWeatherAndPostValue(this)
        viewModel.getForecastAndPostValue(this)
        viewModel.updateForecastData()


        if (!Network.isNetworkAvailable(this)) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Dane mogą być nieaktualne, do aktualizacji wymagane jest połączenie internetowe.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        setSpinner()
        setUpListeners()

        viewModel.updateTime.observe(this) { updateTime ->
            val updateInfoValue = findViewById<TextView>(R.id.updateInfoValue)
            updateInfoValue.text = updateTime
        }

        val lastUpdateTime = appPreferences.lastUpdateTime
        if (!lastUpdateTime.isNullOrEmpty()) {
            val updateInfoValue = findViewById<TextView>(R.id.updateInfoValue)
            updateInfoValue.text = lastUpdateTime
        }
    }

    private fun setUpListeners() {
        val locationSettingsBtn = findViewById<Button>(R.id.locationSettingsBtn)
        val refreshBtn = findViewById<Button>(R.id.refreshBtn)

        locationSettingsBtn.setOnClickListener {
            startActivity(Intent(this, LocationSettingsActivity::class.java))
        }

        refreshBtn.setOnClickListener {
            refreshAction()
        }
    }

    private fun refreshAction() {
        if (!Network.isNetworkAvailable(this)) {
            Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.getCurrentWeatherAndPostValue(this)
        viewModel.getForecastAndPostValue(this)

        restartTimer()

        Toast.makeText(this, "Zaktualizowano dane", Toast.LENGTH_SHORT).show()
    }

    private fun setSpinner() {
        val spinner = findViewById<Spinner>(R.id.metricSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, metrics)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val selectedMetric = appPreferences.selectedMetric

        val selectedIndex = metrics.indexOf(selectedMetric)
        spinner.onItemSelectedListener = null
        spinner.setSelection(selectedIndex, false)
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!Network.isNetworkAvailable(this)) {
            Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedMetric = metrics[position]

        if (selectedMetric != appPreferences.selectedMetric) {
            appPreferences.selectedMetric = selectedMetric
            viewModel.getCurrentWeatherAndPostValue(this)
            viewModel.getForecastAndPostValue(this)
            restartTimer()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nie wybrano żadnej opcji", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(refreshRunnable)
    }
}