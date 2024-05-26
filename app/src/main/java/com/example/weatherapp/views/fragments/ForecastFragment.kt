package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.common.DateConverter.Companion.getDayOfWeek
import com.example.weatherapp.common.IconMap
import com.example.weatherapp.models.forecast.ForecastModel
import com.example.weatherapp.viewmodels.MainViewModel

class ForecastFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().viewModels<MainViewModel>().value

        viewModel.forecast.observe(viewLifecycleOwner, Observer { currentForecast ->

            if (currentForecast == null) {
                Log.i("ForecastFragment", "Weather is null")
                return@Observer
            }

            updateForecast(currentForecast)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    private val allForecastElementsIds = arrayOf(
        intArrayOf(
            R.id.todayDTv,
            R.id.todayIV1,
            R.id.todayIV2,
            R.id.todayDTv,
            R.id.todayNTv
        ),
        intArrayOf(
            R.id.firstDayTv,
            R.id.firstDayIV1,
            R.id.firstDayIV2,
            R.id.firstDayDTv,
            R.id.firstDayNTv
        ),
        intArrayOf(
            R.id.secondDayTv,
            R.id.secondDayIV1,
            R.id.secondDayIV2,
            R.id.secondDayDTv,
            R.id.secondDayNTv
        ),
        intArrayOf(
            R.id.thirdDayTv,
            R.id.thirdDayIV1,
            R.id.thirdDayIV2,
            R.id.thirdDayDTv,
            R.id.thirdDayNTv
        ),
        intArrayOf(
            R.id.fourthDayTv,
            R.id.fourthDayIV1,
            R.id.fourthDayIV2,
            R.id.fourthDayDTv,
            R.id.fourthDayNTv
        ),
    )

    private fun updateForecast(weather: ForecastModel) {

        val filteredForecast = weather.list.filter { listElement ->
            val hour = listElement.dt_txt.split(" ")[1].split(":")[0]
            hour == "18" || hour == "00"
        }

        val mappedForecast = filteredForecast.indices.filter { it % 2 == 0 }.map { index ->
            Pair(filteredForecast[index], filteredForecast.getOrNull(index + 1))
        }

        for (i in mappedForecast.indices) {

            if (i == 0) {
                view?.findViewById<TextView>(allForecastElementsIds[i][0])?.text = "Dzisiaj"
            } else {
                view?.findViewById<TextView>(allForecastElementsIds[i][0])?.text = getDayOfWeek(mappedForecast[i].first.dt)
            }

            val dayIconName = mappedForecast[i].first.weather[0].icon
            val dayIconResId = IconMap.map[dayIconName]
            if (dayIconResId != null)
                view?.findViewById<ImageView>(allForecastElementsIds[i][1])?.setImageResource(dayIconResId)

            val nightIconName = mappedForecast[i].second?.weather?.get(0)?.icon
            val nightIconResId = IconMap.map[nightIconName]
            if (nightIconResId != null)
                view?.findViewById<ImageView>(allForecastElementsIds[i][2])?.setImageResource(nightIconResId)

            view?.findViewById<TextView>(allForecastElementsIds[i][3])?.text = mappedForecast[i].first.main.temp.toInt().toString() + "°C"
            view?.findViewById<TextView>(allForecastElementsIds[i][4])?.text = mappedForecast[i].second?.main?.temp?.toInt().toString() + "°C"
        }
    }
}