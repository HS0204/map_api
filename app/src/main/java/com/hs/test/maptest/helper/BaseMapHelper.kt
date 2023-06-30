package com.hs.test.maptest.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hs.test.maptest.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.hs.test.maptest.NaverFragment
import com.naver.maps.map.util.FusedLocationSource

abstract class BaseMapHelper(private val context: Context, private val naver: NaverFragment? = null) {
    private val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    lateinit var locationSource: FusedLocationSource

    inner class LocationPair(val latitude: Double, val longitude: Double)
    var location = LocationPair(0.0 ,0.0)

    init {
        if (naver != null) {
            locationSource = FusedLocationSource(naver, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    fun getCurrentLocation(callback: (LocationPair?) -> Unit) {
        if (checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(null)
            return
        }

        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val result = LocationPair(location.latitude, location.longitude)
                callback(result)
            } else {
                callback(null)
            }
        }
    }
}