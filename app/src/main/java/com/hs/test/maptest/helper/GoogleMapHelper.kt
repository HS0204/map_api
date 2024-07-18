package com.hs.test.maptest.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.hs.test.maptest.RoutesViewModel
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
     * 카메라 세팅
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
     * 경로 그리기
     */
    fun drawRoutes(track: List<LatLng>?) {
        map.clear()

        if (track.isNullOrEmpty()) return
        map.addPolyline(
            PolylineOptions()
                .addAll(track)
                .addSpan(StyleSpan(Color.GREEN))
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initiateGoogleMap(googleMap)
    }
}
