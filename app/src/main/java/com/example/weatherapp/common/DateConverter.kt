package com.example.weatherapp.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateConverter {
        fun convertUnixToTime(unix: Int): String {
            val date = Date(unix * 1000L)
            val sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getDefault()
            return sdf.format(date)
        }

        fun getDayOfWeek(unix: Int): String {
            val date = Date(unix * 1000L)
            val sdf = SimpleDateFormat("EEEE", Locale("pl", "PL"))
            sdf.timeZone = TimeZone.getDefault()
            return sdf.format(date)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }

        fun getHour(unix: Int): String {
            val date = Date(unix * 1000L)
            val sdf = SimpleDateFormat("HH")
            sdf.timeZone = TimeZone.getDefault()
            return sdf.format(date)
        }
}