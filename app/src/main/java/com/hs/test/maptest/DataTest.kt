package com.hs.test.maptest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DataTest {
    private val _routes: MutableLiveData<String> = MutableLiveData()
    val routes get() = _routes

    private val database = Firebase.database
    private val myRef = database.getReference("test").child("hello")

    fun testReadDb() {
        try {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value as? Map<*, *>
                    processDate(value)
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

    fun testWriteDb() {
        try {
            myRef.push().setValue(LatLng(37.23453, -122.454545))
            Log.e("GoogleMapHelper", "Hello world DB 작성")
        } catch (e: Exception) {
            Log.e("GoogleMapHelper", "Hello world DB 작성 실패")
        }
    }

    private fun processDate(data: Map<*, *>?) {
        if (data == null) return
        for ((key, value) in data) {
            when (value) {
                is Map<*, *> -> {
                    val locationData = value as Map<String, Any>
                    val latitude = locationData["latitude"] as? Double
                    val longitude = locationData["longitude"] as? Double
                    if (latitude != null && longitude != null) {
                        Log.d("GoogleMapHelper", "Location data - Latitude: $latitude, Longitude: $longitude")
                    }
                }
                else -> {
                    Log.d("GoogleMapHelper", "Other data - Key: $key, Value: $value")
                }
            }
        }
    }

}