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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.hs.test.maptest.RoutesViewModel
import java.lang.ref.WeakReference

/**
 * 메인 맵
 */
class MainMap(context: Context, private val routesViewModel: RoutesViewModel) : GoogleMapHelper(context) {

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            // 위치 변경 때마다 감지
            for (location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)
                if (routesViewModel.isTracking.value == true) {
                    routesViewModel.addRoute(latLng)
                    Log.e("GoogleMapHelper", "onLocationResult: $latLng")
                }
            }

            drawRoutes(routesViewModel.currentTrackingPath.value ?: emptyList())
        }
    }

    /**
     * 현위치 확인하여 카메라 세팅
     */
    private fun checkCurrentLocation() {
        if (context == null) return

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return this
                }

                override fun isCancellationRequested(): Boolean {
                    return false
                }
            }).addOnSuccessListener { setCamera(location = it) }
    }

    /**
     * 실시간 위치 감지
     */
    private fun startLocationUpdates() {
        if (context == null) return
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
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

    /**
     * 맵을 그리면서 카메라를 세팅하고 실시간 위치를 감지하도록 오버라이드
     */
    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)
        setMyLocationEnabled(true)
        checkCurrentLocation()
        startLocationUpdates()
    }

    companion object {
        @Volatile
        private var instance: MainMap? = null

        fun getInstance(context: Context, routesViewModel: RoutesViewModel): MainMap {
            return instance ?: synchronized(this) {
                instance ?: MainMap(context, routesViewModel).also { instance = it }
            }
        }
    }
}