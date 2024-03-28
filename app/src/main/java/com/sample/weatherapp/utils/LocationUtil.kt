package com.sample.weatherapp.utils

import android.content.Context
import android.content.IntentSender
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.sample.weatherapp.utils.AppConstants.UPDATE_INTERVAL
import timber.log.Timber

object LocationUtil {

    fun checkLocationSettings(
        context: Context,
        registerForLocationResult: ActivityResultLauncher<IntentSenderRequest>
    ) {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL).build()

        val locationSettingsRequestBuilder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .setAlwaysShow(false)
        LocationServices
            .getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequestBuilder.build())
            .addOnSuccessListener { locationSettingsResponse: LocationSettingsResponse ->
                Timber.d("LocationSettingsResponse: onSuccess: ${locationSettingsResponse.locationSettingsStates}")
            }
            .addOnFailureListener { exception ->
                when ((exception as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        if (exception is ResolvableApiException) {
                            try {
                                registerForLocationResult.launch(
                                    IntentSenderRequest.Builder(
                                        exception.resolution
                                    ).build()
                                )
                            } catch (sendEx: IntentSender.SendIntentException) {
                                sendEx.localizedMessage?.let { context.toast(it) }
                            } catch (classCastException: ClassCastException) {
                                classCastException.localizedMessage?.let { context.toast(it) }
                            }
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Timber.i("Location : Location settings are inadequate, and cannot be fixed here. Dialog not created.")
                    }
                }
            }
    }
}