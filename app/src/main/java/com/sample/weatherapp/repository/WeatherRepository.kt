package com.sample.weatherapp.repository


import com.sample.weatherapp.domain.Resource
import com.sample.weatherapp.domain.weather.WeatherData
import com.sample.weatherapp.remote.ApiDataSource
import com.sample.weatherapp.remote.model.CityLocationData
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
): BaseRepository() {

    suspend fun getWeatherData(lat: String, long: String): Resource<WeatherData> = safeApiCall { apiDataSource.getWeatherData(lat, long) }
    suspend fun getCitiesList(query: String, limit: Int): Resource<CityLocationData> = safeApiCall { apiDataSource.getCityList(query, limit) }

}