package com.hs.test.maptest.data

import com.google.android.gms.maps.model.LatLng

data class RouteInfo(
    val userId: String,
    val date: String,
    val location: List<LatLng>
)
