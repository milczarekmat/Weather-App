package com.example.weatherapp.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat

object Network {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            ContextCompat.getSystemService(context, ConnectivityManager::class.java)
        val network = connectivityManager?.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}