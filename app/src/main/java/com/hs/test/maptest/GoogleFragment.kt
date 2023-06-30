package com.hs.test.maptest

import androidx.core.os.bundleOf
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
    }

    override fun onDestroyView() {
        binding.googleMapView.onDestroy()
        super.onDestroyView()
    }

}