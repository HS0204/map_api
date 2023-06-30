package com.hs.test.maptest.helper

import android.content.Context
import android.location.Location
import com.hs.test.maptest.NaverFragment
import com.hs.test.maptest.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class NaverMapHelper(
    private val context: Context,
    private val fragment: NaverFragment
) : BaseMapHelper(context, fragment), OnMapReadyCallback {

    private lateinit var map: NaverMap

    companion object {
        @Volatile
        private var instance: NaverMapHelper? = null

        fun getInstance(
            context: Context,
            fragment: NaverFragment
        ): NaverMapHelper {
            return instance ?: synchronized(this) {
                instance ?: NaverMapHelper(context, fragment).also { instance = it }
            }
        }
    }

    private fun setNaverMap(naverMap: NaverMap) {
        this.map = naverMap
        this.map.locationSource = locationSource
        this.map.locationTrackingMode = LocationTrackingMode.NoFollow
        this.map.uiSettings.isLocationButtonEnabled = true
    }

    fun setLocationTrackingMode() {
        getCurrentLocation { result ->
            if (result != null) {
                location = result
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude))
                this.map.moveCamera(cameraUpdate)
            } else {
                this.map.locationTrackingMode = LocationTrackingMode.None
            }
        }
    }

    private fun setMarker(location: Location) {
        val markerList = Marker()
        markerList.map = null
        markerList.position = LatLng(location.latitude, location.longitude)
        markerList.icon = OverlayImage.fromResource(R.drawable.baseline_smart_toy_24)
        markerList.captionText = "현 위치"
        markerList.map = map
    }

    override fun onMapReady(naverMap: NaverMap) {
        setNaverMap(naverMap)
    }
}