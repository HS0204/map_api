package com.hs.test.maptest

import androidx.core.os.bundleOf
import com.hs.test.maptest.databinding.FragmentNaverBinding
import com.hs.test.maptest.helper.NaverMapHelper

class NaverFragment : BaseFragment<FragmentNaverBinding>(FragmentNaverBinding::inflate) {

    private lateinit var mapViewHelper: NaverMapHelper

    override fun initView() {
        mapViewHelper = NaverMapHelper.getInstance(context = requireContext(), fragment = this)
        binding.naverMapView.apply {
            onCreate(bundleOf())
            getMapAsync(mapViewHelper)
        }
    }

    override fun onResume() {
        super.onResume()
        mapViewHelper.setLocationTrackingMode()
    }

}