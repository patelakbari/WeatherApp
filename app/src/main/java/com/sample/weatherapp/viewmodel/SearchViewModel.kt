package com.sample.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.weatherapp.domain.Resource
import com.sample.weatherapp.remote.model.CityLocationData
import com.sample.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel(){

    fun getCitiesList(query: String,limit: Int,onSuccess: (CityLocationData) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            when(val result = repository.getCitiesList(query, limit)) {
                is Resource.Success -> {
                    onSuccess(result.data!!)
                }
                is Resource.Error -> {
                    onError(result.message?:"Something went wrong")
                }
            }
        }
    }
}