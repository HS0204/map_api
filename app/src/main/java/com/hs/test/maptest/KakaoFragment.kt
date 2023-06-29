package com.hs.test.maptest

import com.hs.test.maptest.databinding.FragmentKakaoBinding
import com.hs.test.maptest.helper.KakaoMapHelper
import net.daum.mf.map.api.MapView

class KakaoFragment : BaseFragment<FragmentKakaoBinding>(FragmentKakaoBinding::inflate) {

    private lateinit var mapViewHelper: KakaoMapHelper
    private lateinit var mapView: MapView

    override fun initView() {
        mapViewHelper = KakaoMapHelper(requireContext())
        mapView = mapViewHelper.mapView

        binding.kakaoMapView.addView(mapView)
    }

}