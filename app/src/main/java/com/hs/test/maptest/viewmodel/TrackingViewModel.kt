package com.hs.test.maptest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

sealed interface TrackingUiState {
    fun isTracking(): Boolean

    data class Tracking(val track: List<LatLng>) : TrackingUiState {
        override fun isTracking(): Boolean = true
    }

    data class SendTrackingData(val track: List<LatLng>) : TrackingUiState {
        override fun isTracking(): Boolean = false
    }

    object NotTracking : TrackingUiState {
        override fun isTracking(): Boolean = false
    }
}

class TrackingViewModel : ViewModel() {
    // 현재 경로
    private val _currentTrackingPath: MutableLiveData<TrackingUiState> =
        MutableLiveData(TrackingUiState.NotTracking)
    val currentTrackingPath get() = _currentTrackingPath

    /**
     * 현재 경로에 위도, 경도 추가
     * @param latLng 추가할 경로의 위도, 경도
     * @return 추가 후 최신 현재 경로
     */
    fun addRoute(latLng: LatLng) : TrackingUiState {
        if (_currentTrackingPath.value is TrackingUiState.Tracking) {
            _currentTrackingPath.value
        }

        val currentRoutes = if (_currentTrackingPath.value is TrackingUiState.Tracking) {
            (_currentTrackingPath.value as TrackingUiState.Tracking).track.toMutableList()
        } else {
            mutableListOf()
        }

        currentRoutes.add(latLng)
        _currentTrackingPath.postValue(TrackingUiState.Tracking(currentRoutes))

        return _currentTrackingPath.value!!
    }

    /**
     * 현재 경로 그리기
     */
    fun handleTracking() {
        _currentTrackingPath.value?.let { state ->
            when (state) {
                is TrackingUiState.Tracking -> {
                    sendTracking()
                }

                is TrackingUiState.SendTrackingData -> {}

                is TrackingUiState.NotTracking -> {
                    startTracking()
                }
            }
        }
    }

    /**
     * 현재 경로 추적 시작
     */
    private fun startTracking() {
        _currentTrackingPath.postValue(TrackingUiState.Tracking(track = emptyList()))
    }

    /**
     * 현재 추적 중인 경로 저장
     */
    private fun sendTracking() {
        val track = (_currentTrackingPath.value as TrackingUiState.Tracking).track
        _currentTrackingPath.postValue(TrackingUiState.SendTrackingData(track))
    }

    /**
     * 현재 경로 추적 종료
     */
    fun endTracking() {
        _currentTrackingPath.postValue(TrackingUiState.NotTracking)
    }
}