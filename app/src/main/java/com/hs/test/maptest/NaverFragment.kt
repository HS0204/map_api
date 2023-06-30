package com.hs.test.maptest

import androidx.core.os.bundleOf
import com.hs.test.maptest.databinding.FragmentNaverBinding
import com.hs.test.maptest.helper.NaverMapHelper

class NaverFragment : BaseFragment<FragmentNaverBinding>(FragmentNaverBinding::inflate) {

    private lateinit var mapViewHelper: NaverMapHelper

    override fun initView() {
        mapViewHelper = NaverMapHelper(context = requireContext())
        binding.naverMapView.apply {
            onCreate(bundleOf())
            binding.naverMapView.getMapAsync(mapViewHelper)
        }

        binding.test.setOnClickListener {
            mapViewHelper.updateCurrentLocation()
        }
    }

}