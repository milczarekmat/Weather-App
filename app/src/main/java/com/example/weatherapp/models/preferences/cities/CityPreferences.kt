import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CityPreferences(context: Context) {
    private val PREFS_NAME = "com.example.weatherapp.cityprefs"
    private val SELECTED_CITY = "selected_city"
    private val CITY_LIST = "city_list"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var selectedCity: Int
        get() = prefs.getInt(SELECTED_CITY, -1)
        set(value) = prefs.edit().putInt(SELECTED_CITY, value).apply()

    var cityList: MutableList<String>
        get() {
            val gson = Gson()
            val json = prefs.getString(CITY_LIST, null)
            val type = object : TypeToken<MutableList<String>>() {}.type
            return gson.fromJson(json, type) ?: mutableListOf()
        }
        set(value) {
            val gson = Gson()
            val json = gson.toJson(value)
            prefs.edit().putString(CITY_LIST, json).apply()
        }
}