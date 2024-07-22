package com.hs.test.maptest.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.hs.test.maptest.viewmodel.TrackingUiState
import com.hs.test.maptest.viewmodel.TrackingViewModel

/**
 * 메인 맵
 */
class MainMap(context: Context, private val trackingViewModel: TrackingViewModel) : GoogleMapHelper(context) {

    private val locationCallback = object : LocationCallback() {
        // 위치 변경 때마다 감지
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            clearRoutes()

            for (location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)
                handleTracking(latLng)
            }
        }
    }

    /**
     * 지도에 경로 그리기 및 경로 저장 제어
     * @param latLng 최신 감지 위치의 위경도
     */
    private fun handleTracking(latLng: LatLng) {
        trackingViewModel.currentTrackingPath.value?.let { state ->
            if (!state.isTracking()) return@let
            Log.e("GoogleMapHelper", "onLocationResult: $latLng")

            val routeResult = trackingViewModel.addRoute(latLng)
            if (routeResult.isTracking()) {
                drawRoutes((routeResult as TrackingUiState.Tracking).track)
            }
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

        fun getInstance(context: Context, trackingViewModel: TrackingViewModel): MainMap {
            return instance ?: synchronized(this) {
                instance ?: MainMap(context, trackingViewModel).also { instance = it }
            }
        }
    }
}