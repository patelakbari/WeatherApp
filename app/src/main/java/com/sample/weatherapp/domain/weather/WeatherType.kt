package com.sample.weatherapp.domain.weather

import androidx.annotation.DrawableRes
import com.sample.weatherapp.R

data class WeatherType(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)