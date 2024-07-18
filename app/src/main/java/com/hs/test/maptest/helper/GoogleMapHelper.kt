package com.hs.test.maptest.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan
import java.lang.ref.WeakReference

open class GoogleMapHelper(context: Context) : OnMapReadyCallback {

    private val weakContext: WeakReference<Context> = WeakReference(context)
    val context: Context? get() = weakContext.get()

    private lateinit var map: GoogleMap
    val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * 구글 맵 초기화
     */
    private fun initiateGoogleMap(googleMap: GoogleMap) {
        this.map = googleMap

        with(this.map) {
            isBuildingsEnabled = false
            isIndoorEnabled = false
            isTrafficEnabled = false

            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
        }
    }

    /**
     * 내 위치 활성화
     */
    fun setMyLocationEnabled(isEnabled: Boolean) {
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
        this.map.isMyLocationEnabled = isEnabled
    }

    /**
     * 카메라 세팅
     * @param location : 현재 위치
     */
    fun setCamera(location: Location) {
        this.map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 15f
            )
        )
    }

    /**
     * 카메라 세팅
     * @param locations : 경로 리스트
     */
    fun setCamera(locations: List<LatLng>) {
        val builder = LatLngBounds.Builder()
        for (location in locations) {
            builder.include(LatLng(location.latitude, location.longitude))
        }
        val bounds = builder.build()
        val padding = 50
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        this.map.moveCamera(cameraUpdate)
    }

    /**
     * 경로 그리기
     */
    fun drawRoutes(track: List<LatLng>?) {
        this.map.clear()

        if (track.isNullOrEmpty()) return
        this.map.addPolyline(
            PolylineOptions()
                .addAll(track)
                .addSpan(StyleSpan(Color.GREEN))
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initiateGoogleMap(googleMap)
    }
}
