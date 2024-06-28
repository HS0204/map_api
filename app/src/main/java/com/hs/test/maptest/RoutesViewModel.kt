package com.hs.test.maptest

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

class RoutesViewModel : ViewModel() {
    private val _routes: MutableLiveData<List<RouteInfo>> = MutableLiveData()
    val routes get() = _routes

    private val database = Firebase.database

    /**
     * 날짜로 단 건 읽어옴
     * @param userId 사용자 아이디
     * @param date 검색할 날짜 todo: 형식 지정 필요
     */
    fun readRouteInfoFromDB(userId: String, date: String) {
        val db = database.getReference(userId).child(date)

        try {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value as? Map<*, *>
                    if (value != null) {
                        processDate(userId = userId, date = date, data = value)
                    }
                    Log.e("GoogleMapHelper", "$userId [$date] > DB 읽기 성공: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GoogleMapHelper", "$userId [$date] > DB 읽기 실패: ${error.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "Hello world DB 읽기 실패")
        }
    }

    /**
     * 날짜로 단 건 읽어옴
     * @param userId 사용자 아이디
     */
    fun readRouteInfoListFromDB(userId: String) {
        val db = database.getReference(userId)

        try {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value as? Map<*, *>
                    if (value != null) {
                        // todo: 다건 처리 필요.. 날짜별로 묶어서 리스트로 반환
                    }
                    Log.e("GoogleMapHelper", "Hello world DB 읽기 성공: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GoogleMapHelper", "Hello world DB 읽기 실패: ${error.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "Hello world DB 읽기 실패")
        }
    }

    /**
     * 저장
     */
    fun writeLocationToDB(userId: String, date: String) {
        val db = database.getReference(userId).child(date)

        try {
            db.push().setValue(LatLng(37.23453, -122.454545))
            Log.e("GoogleMapHelper", "Hello world DB 작성")
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "Hello world DB 작성 실패")
        }
    }

    /**
     * 다건 전처리
     */

    /**
     * 단건 전처리
     */
    private fun processDate(userId: String, date: String, data: Map<*, *>): RouteInfo {
        val locations = mutableListOf<LatLng>()
        for ((key, value) in data) {
            when (value) {
                is Map<*, *> -> {
                    val locationData = value as? Map<String, Any>
                    val latitude = locationData?.get("latitude") as? Double
                    val longitude = locationData?.get("longitude") as? Double
                    if (latitude != null && longitude != null) {
                        locations.add(LatLng(latitude, longitude))
                        Log.d(
                            "GoogleMapHelper",
                            "Location data - Latitude: $latitude, Longitude: $longitude"
                        )
                    }
                }

                else -> {
                    Log.d("GoogleMapHelper", "Other data - Key: $key, Value: $value")
                }
            }
        }

        return RouteInfo(userId = userId, date = date, location = locations.toList())
    }

}