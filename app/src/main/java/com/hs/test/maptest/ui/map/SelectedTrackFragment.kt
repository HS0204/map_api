package com.hs.test.maptest.ui.map

import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.hs.test.maptest.base.BaseFragment
import com.hs.test.maptest.databinding.FragmentSelectedTrackBinding
import com.hs.test.maptest.helper.SelectedMap

class SelectedTrackFragment : BaseFragment<FragmentSelectedTrackBinding>(FragmentSelectedTrackBinding::inflate) {
    private lateinit var mapHelper: SelectedMap

    private val args by navArgs<SelectedTrackFragmentArgs>()

    override fun initView() {
        mapHelper = SelectedMap.getInstance(
            context = requireContext(),
            track = args.routeInfo.location
        )

        with(binding) {
            googleMapView.apply {
                onCreate(bundleOf())
                onStart()
                getMapAsync(mapHelper)
            }
            track = args.routeInfo
        }
    }

    override fun onDestroyView() {
        binding.googleMapView.onDestroy()
        super.onDestroyView()
    }

}