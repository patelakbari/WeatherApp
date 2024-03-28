package com.sample.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.sample.weatherapp.R
import com.sample.weatherapp.databinding.ActivityMainBinding
import com.sample.weatherapp.domain.weather.WeatherData
import com.sample.weatherapp.domain.weather.WeatherType
import com.sample.weatherapp.utils.AppConstants.getCurrentDate
import com.sample.weatherapp.utils.InternetConnection
import com.sample.weatherapp.utils.LocationUtil
import com.sample.weatherapp.utils.PermissionUtil
import com.sample.weatherapp.utils.gone
import com.sample.weatherapp.utils.onClick
import com.sample.weatherapp.utils.openAllApplicationSettings
import com.sample.weatherapp.utils.registerForIntentSenderRequestsResult
import com.sample.weatherapp.utils.registerForMultiplePermissionsRequestsResult
import com.sample.weatherapp.utils.show
import com.sample.weatherapp.utils.showSnackbarWithAction
import com.sample.weatherapp.utils.toast
import com.sample.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var defaultLocation: LatLng
    private lateinit var registerForLocationResult: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActivityResultLaunchers()
        initClickListener()
        loadGoogleMap()
    }

    override fun onStart() {
        super.onStart()
        checkForLocationPermission()
    }

    private fun loadGoogleMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        defaultLocation = LatLng(19.0760, 72.8777)
        showDefaultLocationOnMap(defaultLocation)
    }


    private fun showDefaultLocationOnMap(latLng: LatLng) {
        moveCamera(latLng)
        animateCamera(latLng)
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15.5f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService() ?: throw IllegalStateException()
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkGpsPermission() {
        if (isLocationEnabled().not()) {
            LocationUtil.checkLocationSettings(this, registerForLocationResult)
        } else {
            fetchWeatherInfo()
        }
    }

    private fun fetchWeatherInfo() {
        if(InternetConnection.isNetworkAvailable(this@MainActivity)){
            binding.linearWeatherInfo.show()
            weatherViewModel.getLocation({
                setWeatherDataUI(it)
            }, onError = {
                toast(it)
            })
        }else{
            binding.linearWeatherInfo.gone()
            binding.root.showSnackbarWithAction(R.string.no_internet,R.string.retry){
                fetchWeatherInfo()
            }
        }
    }

    private fun setWeatherDataUI(data: WeatherData) {
        setWeatherIcon(data.weather.first())
        binding.txtDate.text = getCurrentDate()
        binding.txtCityName.text = data.name
        binding.txtCountryName.text = Locale("", data.sys.country).displayCountry
        val temp = data.main.temp - 273.15
        binding.txtWeatherNumericValue.text = String.format("%.0f", temp)
        binding.txtHumidity.text = data.main.humidity.toString().plus(" %")
        val speed = data.wind.speed * 3.6
        binding.txtWind.text = String.format("%.2f", speed).plus(" km/h")
        binding.txtAtmosphericPressure.text = String.format("%.0f", data.main.pressure.toDouble()).plus("hPa")
        binding.txtWeatherType.text = data.weather.first().main
        defaultLocation = LatLng(data.coord.lat,data.coord.lon)
        showDefaultLocationOnMap(defaultLocation)
    }

    private fun setWeatherIcon(weatherType: WeatherType) {
        when (weatherType.id) {
            in 200..232 -> binding.imgWeather.setImageResource(R.drawable.icon_weather_thunderstorm_cloud)
            in 300..321 -> binding.imgWeather.setImageResource(R.drawable.icon_weather_rain_cloud)
            in 500..531 -> binding.imgWeather.setImageResource(R.drawable.icon_weather_sun_rain_cloud)
            in 701..781 -> binding.imgWeather.setImageResource(R.drawable.icon_weather_cloud_sun)
            in 600..622 -> binding.imgWeather.setImageResource(R.drawable.icon_weather_snow_cloud)
            800 -> binding.imgWeather.setImageResource(R.drawable.icon_weather_sun)
            in 801..804 -> binding.imgWeather.setImageResource(R.drawable.icon_weather_cloud)
            else -> binding.imgWeather.setImageResource(R.drawable.icon_weather_sun)
        }
    }

    private fun initClickListener() {
        binding.txtOpenSetting.onClick {
            if (binding.txtOpenSetting.text.toString() == getString(R.string.open_setting)) {
                openAllApplicationSettings()
            } else {
                checkGpsPermission()
            }
        }
        binding.cardSearchCity.onClick {
            SearchActivity.start(this)
        }
    }

    private fun checkForLocationPermission() {
        if (PermissionUtil.hasPermissions(this, listOf(Manifest.permission.ACCESS_FINE_LOCATION)).not()) {
            requestMultiplePermissions.launch(listOf(Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray())
        } else {
            checkGpsPermission()
        }
    }

    private fun initActivityResultLaunchers() {
        requestMultiplePermissions =
            registerForMultiplePermissionsRequestsResult(onSuccess = { permissionGrantedList ->
                permissionGrantedList.forEach {
                    when (it) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            binding.cardPermissionError.gone()
                            checkGpsPermission()
                        }
                    }
                }
            }, onError = { permissionDeniedList ->
                permissionDeniedList.forEach {
                    when (it) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            showPermissionDeniedError()
                            fetchWeatherInfo()
                        }
                    }
                }
            })

        registerForLocationResult = registerForIntentSenderRequestsResult(onSuccess = {
            binding.cardPermissionError.gone()
        }, onError = {
            showLocationDisableError()
            fetchWeatherInfo()
        })
    }

    private fun showPermissionDeniedError() {
        binding.cardPermissionError.show()
        binding.txtPermissionMsg.text = getString(R.string.permission_denied_msg)
        binding.txtOpenSetting.text = getString(R.string.open_setting)
    }

    private fun showLocationDisableError() {
        binding.cardPermissionError.show()
        binding.txtPermissionMsg.text = getString(R.string.location_disable_msg)
        binding.txtOpenSetting.text = getString(R.string.allow)
    }

    override fun onResume() {
        super.onResume()
        checkPermissionOnResume()
    }

    private fun checkPermissionOnResume() {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (hasAccessFineLocationPermission)
            binding.cardPermissionError.gone()
        else{
            showPermissionDeniedError()
        }
    }
}