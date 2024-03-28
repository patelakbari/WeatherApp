package com.sample.weatherapp.utils

import android.content.Context

object CountryNameByCode {
    fun getCountryNameByCode(context: Context, countryCode: String) : String {
        val resourceName = "country_name_$countryCode"
        val countryNameID = context.resources.getIdentifier(resourceName, "string", context.packageName)

        if(countryNameID != 0) {
            return context.getString(countryNameID)
        }

        return countryCode
    }
}