package com.plcoding.weatherapp.data.remote

import com.sample.weatherapp.domain.weather.WeatherData
import com.sample.weatherapp.domain.weather.WeatherInfo
import com.sample.weatherapp.remote.model.CityLocationData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("appid") appId: String
    ): Response<WeatherData>

    @GET("/geo/1.0/direct")
    suspend fun getCities(
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("appid") appId: String
    ): Response<CityLocationData>
}