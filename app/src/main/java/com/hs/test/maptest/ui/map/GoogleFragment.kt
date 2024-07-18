package com.hs.test.maptest.ui.map

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.hs.test.maptest.R
import com.hs.test.maptest.RoutesViewModel
import com.hs.test.maptest.UserViewModel
import com.hs.test.maptest.base.BaseFragment
import com.hs.test.maptest.databinding.FragmentGoogleBinding
import com.hs.test.maptest.helper.GoogleMapHelper
import com.hs.test.maptest.helper.MainMap
import com.hs.test.maptest.util.getCurrentDateTime

class GoogleFragment : BaseFragment<FragmentGoogleBinding>(FragmentGoogleBinding::inflate) {

    private lateinit var mapViewHelper: MainMap

    private val userViewModel by activityViewModels<UserViewModel>()
    private val routesViewModel by activityViewModels<RoutesViewModel>()

    override fun initView() {
        mapViewHelper = MainMap.getInstance(
            context = requireContext(),
            routesViewModel = routesViewModel
        )

        binding.googleMapView.apply {
            onCreate(bundleOf())
            onStart()
            getMapAsync(mapViewHelper)
        }

        binding.viewModel = routesViewModel
    }

    override fun setObserver() {
        super.setObserver()
        observeTrackingState()
    }

    /**
     * 실시간 위치 감지 상태를 관찰
     */
    private fun observeTrackingState() {
        routesViewModel.isTracking.observe(viewLifecycleOwner) { isObserve ->
            if (isObserve) {
                binding.btnSavePath.setText(R.string.save_path)
                Toast.makeText(requireContext(), "실시간 위치 감지 시작", Toast.LENGTH_SHORT).show()
            } else {
                binding.btnSavePath.setText(R.string.start_save_path)
                if (routesViewModel.currentTrackingPath.value?.isNotEmpty() == true) {
                    routesViewModel.writeRouteToDB(
                        userId = userViewModel.userId,
                        dateTime = getCurrentDateTime()
                    )
                    Toast.makeText(requireContext(), "실시간 위치 기록 저장", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "mapViewHelper is not initialized", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        binding.googleMapView.onDestroy()
        super.onDestroyView()
    }

}