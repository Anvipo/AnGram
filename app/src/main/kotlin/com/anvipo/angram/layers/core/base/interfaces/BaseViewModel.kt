package com.anvipo.angram.layers.core.base.interfaces

import android.content.Intent
import com.anvipo.angram.layers.core.ShowItemsDialogEvent
import com.anvipo.angram.layers.core.ShowSnackMessageEvent
import com.anvipo.angram.layers.core.events.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.SingleLiveEvent

interface BaseViewModel {

    val showErrorEvents: SingleLiveEvent<ShowErrorEventParameters>
    val showViewEvents: SingleLiveEvent<ShowViewEventParameters>
    val showAlertMessageEvents: SingleLiveEvent<ShowAlertMessageEventParameters>
    val showItemsDialogEvents: SingleLiveEvent<ShowItemsDialogEvent>
    val showSnackMessageEvents: SingleLiveEvent<ShowSnackMessageEvent>

    fun onCreateTriggered(): Unit = Unit
    fun onStartTriggered(): Unit = Unit
    fun onResumeTriggered(): Unit = Unit
    fun onPauseTriggered(): Unit = Unit

    fun onBackPressed(): Unit = Unit

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Unit = Unit

    fun onCanceledProgressDialog(): Unit = Unit

}