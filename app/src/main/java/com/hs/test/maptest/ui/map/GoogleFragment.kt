package com.hs.test.maptest.ui.map

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.hs.test.maptest.R
import com.hs.test.maptest.viewmodel.RoutesViewModel
import com.hs.test.maptest.viewmodel.TrackingUiState
import com.hs.test.maptest.viewmodel.TrackingViewModel
import com.hs.test.maptest.viewmodel.UserViewModel
import com.hs.test.maptest.base.BaseFragment
import com.hs.test.maptest.databinding.FragmentGoogleBinding
import com.hs.test.maptest.helper.MainMap
import com.hs.test.maptest.util.getCurrentDateTime

class GoogleFragment : BaseFragment<FragmentGoogleBinding>(FragmentGoogleBinding::inflate) {

    private lateinit var mapViewHelper: MainMap

    private val userViewModel by activityViewModels<UserViewModel>()
    private val trackingViewModel by activityViewModels<TrackingViewModel>()
    private val routesViewModel by activityViewModels<RoutesViewModel>()

    override fun initView() {
        mapViewHelper = MainMap.getInstance(
            context = requireContext(),
            trackingViewModel = trackingViewModel
        )

        binding.googleMapView.apply {
            onCreate(bundleOf())
            onStart()
            getMapAsync(mapViewHelper)
        }

        binding.viewModel = trackingViewModel
    }

    override fun setObserver() {
        super.setObserver()
        trackingViewModel.currentTrackingPath.observe(viewLifecycleOwner) { state ->
            when (state) {
                is TrackingUiState.Tracking -> {
                    binding.btnText = getString(R.string.save_path)
                }

                is TrackingUiState.SendTrackingData -> {
                    binding.btnText = getString(R.string.save_path_to_db)

                    val track = state.track
                    val dateTime = getCurrentDateTime()
                    val isSuccess = routesViewModel.writeRouteToDB(userId = userViewModel.userId, dateTime, track)
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "경로 저장 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "경로 저장 실패", Toast.LENGTH_SHORT).show()
                    }

                    trackingViewModel.endTracking()
                }

                is TrackingUiState.NotTracking -> {
                    binding.btnText = getString(R.string.start_save_path)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // 실시간 위치 감지 삭제
        if (::mapViewHelper.isInitialized) {
            mapViewHelper.removeLocationUpdates()
        } else {
            Toast.makeText(requireContext(), "mapViewHelper is not initialized", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        binding.googleMapView.onDestroy()
        super.onDestroyView()
    }

}