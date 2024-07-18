package com.hs.test.maptest.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteInfo(
    val userId: String,         // userId > key
    val date: String,           // date > key
    val location: List<LatLng>  // 위경도 리스트
) : Parcelable
