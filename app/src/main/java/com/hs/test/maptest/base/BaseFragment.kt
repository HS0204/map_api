package com.hs.test.maptest.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding>(
    private val inflate: (LayoutInflater) -> B
) : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
    }

    abstract fun initView()
    open fun setObserver() {}
    protected fun <T> observe(liveData: LiveData<T>, observer: Observer<T>) {
        liveData.observe(viewLifecycleOwner, observer)
    }

    protected fun removeObservers(vararg liveData: LiveData<*>) {
        for (data in liveData) {
            data.removeObservers(viewLifecycleOwner)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
        _binding = null
    }
}