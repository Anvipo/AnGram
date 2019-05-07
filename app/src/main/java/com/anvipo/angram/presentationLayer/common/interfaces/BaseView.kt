package com.anvipo.angram.presentationLayer.common.interfaces

import com.arellomobile.mvp.MvpView

interface BaseView : Presentable, MvpView {

    fun showToastMessage(text: String)
    fun showAlertMessage(text: String)

}