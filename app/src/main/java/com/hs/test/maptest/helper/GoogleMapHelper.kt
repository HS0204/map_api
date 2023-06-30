package com.hs.test.maptest.helper

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

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

    override fun onMapReady(googleMap: GoogleMap) {
        setGoogleMap(googleMap)
    }
}