package com.sample.weatherapp.domain

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}