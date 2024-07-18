package com.hs.test.maptest.helper

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class SelectedMap(context: Context, private val track: List<LatLng>) : GoogleMapHelper(context) {

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)
        setMyLocationEnabled(false)
        drawRoutes(track)
        setCamera(track)
    }

    companion object {
        @Volatile
        private var instance: SelectedMap? = null

        fun getInstance(context: Context, track: List<LatLng>): SelectedMap {
            return instance ?: synchronized(this) {
                instance ?: SelectedMap(context, track).also { instance = it }
            }
        }
    }
}