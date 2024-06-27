package com.hs.test.maptest.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan

class GoogleMapHelper(
    private val context: Context
) : BaseMapHelper(context), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // todo: 테스트 위치 임
    private val routes: MutableList<LatLng> = mutableListOf(LatLng(37.774785, -122.454545))

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            // 위치 변경 때마다 감지
            for (location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)
                routes.add(latLng)
                Log.e("GoogleMapHelper", "onLocationResult: $latLng")
            }

            drawRoutes()
        }
    }

    private fun drawRoutes() {
        this@GoogleMapHelper.map.clear()
        this@GoogleMapHelper.map.addPolyline(
            PolylineOptions()
                .addAll(routes)
                .addSpan(StyleSpan(Color.GREEN))
        )
    }

    /**
     * 실시간 위치 감지
     */
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.create()
            .setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    /**
     * 실시간 위치 감지 삭제
     */
    fun removeLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setGoogleMap(googleMap: GoogleMap) {
        this.map = googleMap

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        with(this.map) {
            isMyLocationEnabled = true
            isBuildingsEnabled = false
            isIndoorEnabled = false
            isTrafficEnabled = false

            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
        }
    }

    private fun setCamera() {
        getCurrentLocation { result ->
            if (result != null) {
                location = result
                this.map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), 15f
                    )
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        setGoogleMap(googleMap)
        setCamera()
        startLocationUpdates()
    }

    companion object {
        @Volatile
        private var instance: GoogleMapHelper? = null

        fun getInstance(context: Context): GoogleMapHelper {
            return instance ?: synchronized(this) {
                instance ?: GoogleMapHelper(context).also { instance = it }
            }
        }
    }
}