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
        fun getInstance(context: Context, track: List<LatLng>): SelectedMap {
            return SelectedMap(context, track)
        }
    }
}