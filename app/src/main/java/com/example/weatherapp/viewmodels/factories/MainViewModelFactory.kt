package com.example.weatherapp.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.models.preferences.CityPreferences
import com.example.weatherapp.viewmodels.MainViewModel

class MainViewModelFactory(private val cityPreferences: CityPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(cityPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}