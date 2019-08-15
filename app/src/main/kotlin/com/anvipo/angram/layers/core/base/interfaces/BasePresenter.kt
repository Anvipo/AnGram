package com.anvipo.angram.layers.core.base.interfaces

import android.content.Intent

interface BasePresenter {

    fun onStartTriggered(): Unit = Unit

    fun onResumeTriggered(): Unit = Unit
    fun onPauseTriggered(): Unit = Unit
    fun onBackPressed(): Unit = Unit

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Unit = Unit

    fun coldStart(): Unit = Unit
    fun hotStart(): Unit = Unit

    fun onCanceledProgressDialog(): Unit = Unit

}