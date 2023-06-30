package com.hs.test.maptest.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

abstract class BaseMapHelper(private val context: Context) {
    val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    inner class LocationPair(val latitude: Double, val longitude: Double)
    var location = LocationPair(0.0 ,0.0)

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