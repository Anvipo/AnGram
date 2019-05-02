package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.R

abstract class FlowFragment : BaseFragment() {

    abstract fun onExit()


    override val layoutRes: Int = R.layout.layout_container


    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }


    /// PRIVATE


    private val currentFragment
        get() = childFragmentManager.findFragmentById(R.id.container) as? BaseFragment


}