package com.sample.weatherapp.domain.weather

import java.time.LocalDateTime

data class WeatherData(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: MainData,
    val name: String,
    val sys: SunData,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherType>,
    val wind: Wind
)