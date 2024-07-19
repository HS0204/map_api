package com.hs.test.maptest.ui.routeList

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hs.test.maptest.R
import com.hs.test.maptest.RouteUiState
import com.hs.test.maptest.RoutesViewModel
import com.hs.test.maptest.UserViewModel
import com.hs.test.maptest.base.BaseFragment
import com.hs.test.maptest.data.RouteInfo
import com.hs.test.maptest.databinding.FragmentRoutesBinding

class RoutesFragment : BaseFragment<FragmentRoutesBinding>(FragmentRoutesBinding::inflate),
    OnRouteClickListener {
    private val userViewModel by activityViewModels<UserViewModel>()
    private val routesViewModel by activityViewModels<RoutesViewModel>()

    private val adapter = RouteListAdapter(interaction = this)

    override fun initView() {
        binding.viewModel = routesViewModel
        binding.rvRouteList.adapter = adapter
        routesViewModel.readRouteInfoListFromDB(userId = userViewModel.userId)
    }

    override fun setObserver() {
        super.setObserver()
        routesViewModel.routeList.observe(viewLifecycleOwner) { state ->
            binding.routeListUiState = state

            if (state is RouteUiState.Success) {
                val routeList = state.routeInfo
                adapter.submitList(routeList)
            }
        }
    }

    override fun onRouteClick(routeInfo: RouteInfo) {
        // todo: 내비게이션 구조에 대한 개선 작업 필요
        findNavController().currentDestination?.getAction(R.id.action_routesFragment_to_selectedTrackFragment)
            ?.let {
                val action =
                    RoutesFragmentDirections.actionRoutesFragmentToSelectedTrackFragment(routeInfo)
                findNavController().navigate(action)
            }
    }
}