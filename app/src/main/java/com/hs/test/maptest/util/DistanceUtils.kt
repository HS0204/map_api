package com.hs.test.maptest.util

import com.google.android.gms.maps.model.LatLng
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * 경로의 총 거리 계산
 */
fun List<LatLng>.getTotalDistance(): Double {
    var totalDistance = 0.0
    for (i in 0 until this.size - 1) {
        totalDistance += getDistance(this[i], this[i + 1])
    }
    return totalDistance
}

/**
 * 두 지점 간의 거리 계산
 * https://en.wikipedia.org/wiki/Haversine_formula
 * @param location1 지점1
 * @param location2 지점2
 * @return 거리 (km)
 */
private fun getDistance(location1: LatLng, location2: LatLng): Double {
    val (lat1, lon1) = Pair(location1.latitude, location1.longitude)
    val (lat2, lon2) = Pair(location2.latitude, location2.longitude)

    val theta = lon1 - lon2
    var dist = sin(Math.toRadians(lat1)) * sin(Math.toRadians(lat2)) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            cos(Math.toRadians(theta))
    dist = acos(dist)
    dist = Math.toDegrees(dist)
    dist *= 60 * 1.1515
    return dist
}