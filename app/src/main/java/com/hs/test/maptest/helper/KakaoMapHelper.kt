package com.hs.test.maptest.helper

import android.content.Context
import com.hs.test.maptest.NaverFragment
import net.daum.mf.map.api.MapView

class KakaoMapHelper(
    context: Context,
) : BaseMapHelper(context) {

    var mapView: MapView = MapView(context)

    companion object {
        @Volatile
        private var instance: KakaoMapHelper? = null

        fun getInstance(
            context: Context,
        ): KakaoMapHelper {
            return instance ?: synchronized(this) {
                instance ?: KakaoMapHelper(context).also { instance = it }
            }
        }
    }

    init {

    }

    fun setView() {
        
    }

}