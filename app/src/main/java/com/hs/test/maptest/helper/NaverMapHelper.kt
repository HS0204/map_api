package com.hs.test.maptest.helper

import android.content.Context
import com.hs.test.maptest.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.hs.test.maptest.NaverFragment
import com.hs.test.maptest.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class NaverMapHelper(
    private val context: Context
) : BaseMapHelper(context), OnMapReadyCallback {

    private lateinit var map: NaverMap
    private val userMarker = Marker()

    init {
        updateCurrentLocation()
    }

    private fun setNaverMap(naverMap: NaverMap) {
        this.map = naverMap
        this.map.locationTrackingMode = LocationTrackingMode.Follow
    }

    fun updateCurrentLocation() {
        getCurrentLocation { result ->
            if (result != null) {
                location = result
                setCurrentLocationCam()
                setUserMarker()
            }
        }
    }

    private fun setCurrentLocationCam() {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude)).animate(
            CameraAnimation.Easing)
        this.map.moveCamera(cameraUpdate)
    }

    private fun setUserMarker() {
        userMarker.map = null
        userMarker.position = LatLng(location.latitude, location.longitude)
        userMarker.icon = OverlayImage.fromResource(R.drawable.baseline_smart_toy_24)
        userMarker.captionText = "현 위치"
        userMarker.map = map
    }

    override fun onMapReady(naverMap: NaverMap) {
        setNaverMap(naverMap)
    }
}