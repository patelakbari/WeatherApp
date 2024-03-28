package com.sample.weatherapp.remote

import com.plcoding.weatherapp.data.remote.WeatherApi
import com.sample.weatherapp.BuildConfig
import javax.inject.Inject

class ApiDataSource @Inject constructor(
    private val apiService: WeatherApi
) {

    suspend fun getWeatherData(lat: String, log: String) =
        apiService.getWeatherData(lat, log, BuildConfig.APP_ID)

    suspend fun getCityList(query: String, limit: Int) =
        apiService.getCities(query, limit, BuildConfig.APP_ID)

}