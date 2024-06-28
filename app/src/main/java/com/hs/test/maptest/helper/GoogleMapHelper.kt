package com.hs.test.maptest.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
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
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.lang.ref.WeakReference

class GoogleMapHelper(context: Context) : OnMapReadyCallback {

    private val weakContext: WeakReference<Context> = WeakReference(context)
    private val context: Context? get() = weakContext.get()

    private lateinit var map: GoogleMap
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // todo: 테스트 위치 임시
    private val routes: MutableList<LatLng> = mutableListOf()
//    private val routes: MutableList<LatLng> = mutableListOf(LatLng(37.774785, -122.454545))

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
     * 구글 맵 초기화
     */
    private fun initiateGoogleMap(googleMap: GoogleMap) {
        this.map = googleMap

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
        with(this.map) {
            isMyLocationEnabled = true
            isBuildingsEnabled = false
            isIndoorEnabled = false
            isTrafficEnabled = false

            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
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
     * 최초 카메라 세팅
     */
    private fun setCamera(location: Location) {
        this.map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 15f
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initiateGoogleMap(googleMap)
        checkCurrentLocation()
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