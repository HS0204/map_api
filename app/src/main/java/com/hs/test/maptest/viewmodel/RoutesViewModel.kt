package com.hs.test.maptest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hs.test.maptest.data.RouteInfo

sealed interface RouteUiState {
    data class Success(val routeInfo: List<RouteInfo>) : RouteUiState
    object NoData : RouteUiState
    object Error : RouteUiState
    object Loading : RouteUiState
}

class RoutesViewModel : ViewModel() {
    // DB에서 읽어온 경로 리스트
    private val _routeList: MutableLiveData<RouteUiState> = MutableLiveData(RouteUiState.Loading)
    val routeList get() = _routeList

    private val database = Firebase.database

    /**
     * 날짜로 단 건 읽어옴
     * @param userId 사용자 아이디
     * @param date 검색할 날짜
     */
    fun readRouteInfoFromDB(userId: String, date: String) {
        val db = database.getReference(userId).child(date)

        try {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value as? Map<*, *>
                    if (value != null) {
                        parseDataToRouteInfo(userId = userId, date = date, data = value)
                    }
                    Log.i("GoogleMapHelper", "$userId [$date] > DB 읽기 성공: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GoogleMapHelper", "$userId [$date] > DB 읽기 실패: ${error.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "$userId DB 읽기 실패: ${e.message}")
        }
    }

    /**
     * 유저 id로 전체 건 읽어옴
     * @param userId 사용자 아이디
     */
    fun readRouteInfoListFromDB(userId: String) {
        val db = database.getReference(userId)

        try {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value as? Map<*, *>
                    if (value != null) {
                        _routeList.postValue(
                            RouteUiState.Success(
                                parseDataToRouteInfoList(
                                    userId = userId,
                                    data = value
                                )
                            )
                        )
                    } else {
                        _routeList.postValue(RouteUiState.NoData)
                    }
                    Log.i("GoogleMapHelper", "$userId DB 읽기 성공: ${snapshot.value}")
                }

                override fun onCancelled(error: DatabaseError) {
                    _routeList.postValue(RouteUiState.Error)
                    Log.e("GoogleMapHelper", "$userId DB 읽기 실패: ${error.message}")
                }
            })
        } catch (e: Exception) {
            _routeList.postValue(RouteUiState.Error)
            Log.e("GoogleMapHelper", "$userId DB 읽기 실패: ${e.message}")
        }
    }

    /**
     * DB에 저장
     * @param userId 사용자 아이디
     * @param dateTime 저장할 날짜
     * @return 성공 여부
     */
    fun writeRouteToDB(userId: String, dateTime: String, track: List<LatLng>): Boolean {
        val db = database.getReference(userId).child(dateTime)

        return try {
            track.map { location -> db.push().setValue(location) }
            Log.i("GoogleMapHelper", "$userId 에 $dateTime DB 작성")
            true
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "$userId 에 $dateTime DB 작성 실패")
            false
        }
    }

    /**
     * 단건 전처리
     */
    private fun parseDataToRouteInfo(userId: String, date: String, data: Map<*, *>): RouteInfo {
        val locations = mutableListOf<LatLng>()

        try {
            for ((_, value) in data) {
                val locationData =
                    value as? Map<String, Any> ?: return RouteInfo(userId, date, emptyList())
                val latitude = locationData["latitude"] as? Double
                val longitude = locationData["longitude"] as? Double

                if (latitude != null && longitude != null) {
                    locations.add(LatLng(latitude, longitude))
                    Log.d(
                        "GoogleMapHelper",
                        "Location data - Latitude: $latitude, Longitude: $longitude"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "DB 데이터 전처리 실패: ${e.message}")
        }

        return RouteInfo(userId = userId, date = date, location = locations.toList())
    }

    /**
     * 다건 전처리
     */
    private fun parseDataToRouteInfoList(userId: String, data: Map<*, *>): List<RouteInfo> {
        val routeInfoList = mutableListOf<RouteInfo>()

        try {
            for ((date, locations) in data) {
                val locationData = locations as? Map<String, Map<*, *>> ?: return emptyList()
                val locationList = mutableListOf<LatLng>()

                for ((_, latlng) in locationData) {
                    val latitude = latlng["latitude"] as? Double
                    val longitude = latlng["longitude"] as? Double
                    if (latitude != null && longitude != null) {
                        locationList.add(LatLng(latitude, longitude))
                        Log.d(
                            "GoogleMapHelper",
                            "Location data - Latitude: $latitude, Longitude: $longitude"
                        )
                    }
                }

                routeInfoList.add(
                    RouteInfo(userId = userId, date = date.toString(), location = locationList)
                )
            }
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "DB 데이터 전처리 실패: ${e.message}")
        }

        return routeInfoList
    }

}