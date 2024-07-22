package com.hs.test.maptest.util

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hs.test.maptest.R
import com.hs.test.maptest.viewmodel.RouteUiState

object BindingAdapter {
    /**
     * 경로 화면 내 데이터 로딩 상태에 따라 TextView 표시
     */
    @JvmStatic
    @BindingAdapter("app:setRouteListTextView")
    fun TextView.setRouteListTextView(state: RouteUiState) {
        visibility = if (state is RouteUiState.Success || state is RouteUiState.Loading) { View.GONE } else { View.VISIBLE }
        text = when (state) {
            is RouteUiState.NoData -> context.getString(R.string.no_route)
            is RouteUiState.Error -> context.getString(R.string.has_error)
            else -> ""
        }
    }

    @JvmStatic
    @BindingAdapter("app:setProgressVisibility")
    fun ProgressBar.setProgressVisibility(state: RouteUiState) {
        visibility = if (state is RouteUiState.Loading) { View.VISIBLE } else { View.GONE }
    }
}