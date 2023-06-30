package com.hs.test.maptest

import androidx.core.os.bundleOf
import com.hs.test.maptest.databinding.FragmentGoogleBinding
import com.hs.test.maptest.helper.GoogleMapHelper


class GoogleFragment : BaseFragment<FragmentGoogleBinding>(FragmentGoogleBinding::inflate) {

        private lateinit var mapViewHelper: GoogleMapHelper
    override fun initView() {
        mapViewHelper = GoogleMapHelper(context = requireContext())

        binding.googleMapView.apply {
            onCreate(bundleOf())
            getMapAsync(mapViewHelper)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.googleMapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        binding.googleMapView.onStop()
    }
    override fun onResume() {
        super.onResume()
        binding.googleMapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        binding.googleMapView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        binding.googleMapView.onLowMemory()
    }
    override fun onDestroy() {
        binding.googleMapView.onDestroy()
        super.onDestroy()
    }

}