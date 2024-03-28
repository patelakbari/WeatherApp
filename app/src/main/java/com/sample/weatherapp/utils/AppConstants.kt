package com.sample.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppConstants {

    const val UPDATE_INTERVAL = 3000L
    const val SELECTED_CITY_DETAIL = "selected_city_detail"


    enum class ErrorType { PERMISSION, API }

    fun getCurrentDate(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
        return dateFormat.format(currentDate)
    }
}