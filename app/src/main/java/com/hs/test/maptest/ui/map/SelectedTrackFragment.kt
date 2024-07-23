package com.hs.test.maptest.ui.map

import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.hs.test.maptest.R
import com.hs.test.maptest.base.BaseFragment
import com.hs.test.maptest.databinding.FragmentSelectedTrackBinding
import com.hs.test.maptest.helper.SelectedMap
import com.hs.test.maptest.util.formatTime
import com.hs.test.maptest.util.getTotalDistance

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
            date = args.routeInfo.date.formatTime()
            totalDistance = getString(R.string.total_distance, args.routeInfo.totalDistance)
        }
    }

    override fun onDestroyView() {
        binding.googleMapView.onDestroy()
        super.onDestroyView()
    }

}