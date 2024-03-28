package com.sample.weatherapp.viewmodel


import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sample.weatherapp.domain.LocationTracker
import com.sample.weatherapp.domain.Resource
import com.sample.weatherapp.domain.weather.WeatherData
import com.sample.weatherapp.domain.weather.WeatherInfo
import com.sample.weatherapp.repository.WeatherRepository
import com.sample.weatherapp.utils.AppConstants.ErrorType
import com.sample.weatherapp.utils.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val repository: WeatherRepository
) : ViewModel() {

    fun getLocation(onSuccess: (WeatherData) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                val latLng = LatLng(location.latitude,location.longitude)
                getWeatherInfo(onSuccess,onError,latLng)
            } ?: kotlin.run {
                val latLng = LatLng(19.0760,72.8777)
                getWeatherInfo(onSuccess,onError,latLng)
            }
        }
    }

    private fun getWeatherInfo(onSuccess: (WeatherData) -> Unit, onError: (String) -> Unit, latlng: LatLng) {
        Timber.d("Method call")
        viewModelScope.launch {
            var lat = latlng.latitude.toString()
            var lon = latlng.longitude.toString()
            if (SharedPreferenceUtil.slectedCityLocation.isNotEmpty()) {
                lat = SharedPreferenceUtil.slectedCityLocation.split(" ")[0]
                lon = SharedPreferenceUtil.slectedCityLocation.split(" ")[1]
                SharedPreferenceUtil.slectedCityLocation = ""
            }
            when (val result = repository.getWeatherData(lat, lon)) {
                is Resource.Success -> {
                    onSuccess(result.data!!)
                }

                is Resource.Error -> {
                    onError(result.message ?: "Something went wrong")
                }
            }
        }
    }
}