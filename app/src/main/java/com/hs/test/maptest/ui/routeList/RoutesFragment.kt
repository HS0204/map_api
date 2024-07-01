package com.hs.test.maptest.ui.routeList

import androidx.fragment.app.activityViewModels
import com.hs.test.maptest.RoutesViewModel
import com.hs.test.maptest.UserViewModel
import com.hs.test.maptest.base.BaseFragment
import com.hs.test.maptest.databinding.FragmentRoutesBinding

class RoutesFragment : BaseFragment<FragmentRoutesBinding>(FragmentRoutesBinding::inflate) {
    private val userViewModel by activityViewModels<UserViewModel>()
    private val routesViewModel by activityViewModels<RoutesViewModel>()

    override fun initView() {
        routesViewModel.readRouteInfoListFromDB(userId = userViewModel.userId)
        binding.viewModel = routesViewModel
    }

    override fun setObserver() {
        super.setObserver()
        routesViewModel.routeList.observe(viewLifecycleOwner) { routeList ->
            binding.tvRouteList.text = routeList.toString()
        }
    }
}