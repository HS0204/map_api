package com.hs.test.maptest.helper

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class GoogleMapHelper(
    private val context: Context
): BaseMapHelper(context), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    companion object {
        @Volatile
        private var instance: GoogleMapHelper? = null

        fun getInstance(context: Context): GoogleMapHelper {
            return instance ?: synchronized(this) {
                instance ?: GoogleMapHelper(context).also { instance = it }
            }
        }
    }

    private fun setGoogleMap(googleMap: GoogleMap) {
        this.map = googleMap
    }

    private fun setCamera() {
        getCurrentLocation { result ->
            if (result != null) {
                location = result
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        setGoogleMap(googleMap)
        setCamera()
        this.map.isMyLocationEnabled = true
    }
}