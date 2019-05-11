package com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter

interface AppPresenter : BasePresenter {

    fun onResumeFragments()
    fun onPause()

}