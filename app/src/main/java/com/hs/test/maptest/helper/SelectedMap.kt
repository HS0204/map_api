package com.hs.test.maptest.helper

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class SelectedMap(context: Context, private val track: List<LatLng>) : GoogleMapHelper(context) {

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)
        // todo: 트랙을 한 눈에 볼 수 있는 지점 및 줌 레벨 찾기
//        setCamera()
        drawRoutes(track)
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