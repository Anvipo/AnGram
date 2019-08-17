package com.anvipo.angram.layers.core.base.interfaces

import android.content.Intent
import com.anvipo.angram.layers.core.ShowItemsDialogEvent
import com.anvipo.angram.layers.core.ShowSnackMessageEvent
import com.anvipo.angram.layers.core.events.ShowAlertMessageEvent
import com.anvipo.angram.layers.core.events.ShowErrorEvent
import com.anvipo.angram.layers.core.events.ShowViewEvent
import com.anvipo.angram.layers.core.events.SingleLiveEvent

interface BaseViewModel {

    val showErrorEvents: SingleLiveEvent<ShowErrorEvent>
    val showViewEvents: SingleLiveEvent<ShowViewEvent>
    val showAlertMessageEvents: SingleLiveEvent<ShowAlertMessageEvent>
    val showItemsDialogEvents: SingleLiveEvent<ShowItemsDialogEvent>
    val showSnackMessageEvents: SingleLiveEvent<ShowSnackMessageEvent>

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