package com.anvipo.angram.presentationLayer.common.interfaces

import android.content.Intent

interface BasePresenter {

    fun coldStart(): Unit = Unit
    fun hotStart(): Unit = Unit

    fun onStartTriggered(): Unit = Unit

    fun onResumeTriggered(): Unit = Unit

    fun onPauseTriggered(): Unit = Unit

    fun onBackPressed(): Unit = Unit

    fun onCanceledProgressDialog(): Unit = Unit

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Unit = Unit

}