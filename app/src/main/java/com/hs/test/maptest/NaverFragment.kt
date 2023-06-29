package com.hs.test.maptest

import com.hs.test.maptest.databinding.FragmentNaverBinding
import com.naver.maps.map.NaverMapSdk

class NaverFragment : BaseFragment<FragmentNaverBinding>(FragmentNaverBinding::inflate) {
    override fun initView() {
        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_NATIVE_APP_KEY)
    }

}