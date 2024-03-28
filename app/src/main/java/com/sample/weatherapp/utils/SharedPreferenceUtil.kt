package com.sample.weatherapp.utils

import android.content.Context
import com.sample.weatherapp.MyApp
import com.sample.weatherapp.R
import com.sample.weatherapp.utils.AppConstants.SELECTED_CITY_DETAIL

object SharedPreferenceUtil {

    private val sharedPreference = MyApp.context.getSharedPreferences(MyApp.context.packageName, Context.MODE_PRIVATE)

    public var slectedCityLocation: String
        get() = getStringKey(SELECTED_CITY_DETAIL)
        set(value) = setStringKey(SELECTED_CITY_DETAIL, value)

    fun getStringKey(key: String, defaultValue: String = ""): String = sharedPreference.getString(key, defaultValue)!!

    fun setStringKey(key: String, value: String) {
        sharedPreference.edit().putString(key, value).apply()
    }

    private fun getBooleanKey(key: String, defaultValue: Boolean = false): Boolean = sharedPreference.getBoolean(key, defaultValue)
    private fun setBooleanKey(key: String, value: Boolean) {
        sharedPreference.edit().putBoolean(key, value).apply()
    }

    fun getIntKey(key: String, defaultValue: Int = -1): Int = sharedPreference.getInt(key, defaultValue)
    fun setIntKey(key: String, value: Int) {
        sharedPreference.edit().putInt(key, value).apply()
    }

    fun getFloatKey(key: String, defaultValue: Float = 0f): Float = sharedPreference.getFloat(key, defaultValue)
    fun setFloatKey(key: String, value: Float) {
        sharedPreference.edit().putFloat(key, value).apply()
    }

    fun getLongKey(key: String, defaultValue: Long = 0): Long = sharedPreference.getLong(key, defaultValue)
    fun setLongKey(key: String, value: Long) {
        sharedPreference.edit().putLong(key, value).apply()
    }

    fun clearSharedPreferences() {
        sharedPreference.edit().clear().apply()
    }
}