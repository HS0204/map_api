package com.hs.test.maptest.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.hs.test.maptest.util.formatTime
import com.hs.test.maptest.util.getTotalDistance
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteInfo(
    val userId: String,           // userId > key
    val date: String,             // date > key
    val location: List<LatLng>,   // 위경도 리스트
    val totalDistance: Double = location.getTotalDistance()     // 총 거리
) : Parcelable
