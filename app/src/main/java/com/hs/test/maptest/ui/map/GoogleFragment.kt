package com.hs.test.maptest.ui.map

import androidx.core.os.bundleOf
import com.hs.test.maptest.RoutesViewModel
import com.hs.test.maptest.base.BaseFragment
import com.hs.test.maptest.databinding.FragmentGoogleBinding
import com.hs.test.maptest.helper.GoogleMapHelper

class GoogleFragment : BaseFragment<FragmentGoogleBinding>(FragmentGoogleBinding::inflate) {

    private lateinit var mapViewHelper: GoogleMapHelper

    override fun initView() {
        mapViewHelper = GoogleMapHelper.getInstance(context = requireContext())

        binding.googleMapView.apply {
            onCreate(bundleOf())
            onStart()
            getMapAsync(mapViewHelper)
        }
        RoutesViewModel().readRouteInfoFromDB(userId = "test", date = "hello")
    }

    override fun onPause() {
        super.onPause()
        // 실시간 위치 감지 삭제
        mapViewHelper.removeLocationUpdates()
    }

    override fun onDestroyView() {
        binding.googleMapView.onDestroy()
        super.onDestroyView()
    }

}