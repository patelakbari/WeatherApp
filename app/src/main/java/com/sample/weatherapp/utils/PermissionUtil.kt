package com.sample.weatherapp.utils

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionUtil {

    companion object {

        fun hasPermissions(context: Context, permissions: List<String>): Boolean {
            permissions.forEach { permission ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }
    }
}

fun ComponentActivity.registerForMultiplePermissionsRequestsResult(
    onSuccess: (List<String>) -> Unit,
    onError: (List<String>) -> Unit
) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (PermissionUtil.hasPermissions(this, permissions.keys.toList())) {
            val permissionGrantedList = permissions.filter { it.value }.keys.toList()
            onSuccess(permissionGrantedList)
        } else {
            val permissionDeniedList = permissions.filter { it.value.not() }.keys.toList()
            onError(permissionDeniedList)
        }
    }

fun ComponentActivity.registerForIntentSenderRequestsResult(
    onSuccess: () -> Unit,
    onError: () -> Unit
) =
    registerForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            onSuccess()
        } else {
            onError()
        }
    }

fun Activity.openAllApplicationSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}