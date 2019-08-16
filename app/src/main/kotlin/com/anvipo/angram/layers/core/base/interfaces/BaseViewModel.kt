package com.anvipo.angram.layers.core.base.interfaces

import android.content.Intent
import com.anvipo.angram.layers.core.events.ErrorEvent
import com.anvipo.angram.layers.core.events.ShowProgressEvent
import com.anvipo.angram.layers.core.events.ShowAlertMessageEvent
import com.anvipo.angram.layers.core.events.SingleLiveEvent

interface BaseViewModel {

    val errorEvents: SingleLiveEvent<ErrorEvent>
    val showProgressEvents: SingleLiveEvent<ShowProgressEvent>
    val showAlertMessageEvents: SingleLiveEvent<ShowAlertMessageEvent>

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