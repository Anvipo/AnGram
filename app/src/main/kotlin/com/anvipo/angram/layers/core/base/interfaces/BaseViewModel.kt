package com.anvipo.angram.layers.core.base.interfaces

import android.content.Intent
import android.os.Bundle
import com.anvipo.angram.layers.core.ShowItemsDialogEventParameters
import com.anvipo.angram.layers.core.ShowSnackMessageEventParameters
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowToastMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters

interface BaseViewModel {

    val showViewEvents: SingleLiveEvent<ShowViewEventParameters>
    val showItemsDialogEvents: SingleLiveEvent<ShowItemsDialogEventParameters>
    val showErrorEvents: SingleLiveEvent<ShowErrorEventParameters>
    val showAlertMessageEvents: SingleLiveEvent<ShowAlertMessageEventParameters>
    val showSnackMessageEvents: SingleLiveEvent<ShowSnackMessageEventParameters>
    val showToastMessageEvents: SingleLiveEvent<ShowToastMessageEventParameters>
    val showConnectionSnackMessageEvents: SingleLiveEvent<ShowSnackMessageEventParameters>

    fun onColdStart(): Unit = Unit
    fun onHotStart(savedInstanceState: Bundle): Unit = Unit
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