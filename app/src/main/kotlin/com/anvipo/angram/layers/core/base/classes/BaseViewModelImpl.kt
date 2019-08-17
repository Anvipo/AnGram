package com.anvipo.angram.layers.core.base.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anvipo.angram.layers.core.ShowItemsDialogEventParameters
import com.anvipo.angram.layers.core.ShowSnackMessageEventParameters
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.base.interfaces.HasMyCoroutineBuilders
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowToastEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModelImpl :
    ViewModel(),
    BaseViewModel,
    HasMyCoroutineBuilders,
    HasLogger {

    final override val showViewEvents: SingleLiveEvent<ShowViewEventParameters> =
        SingleLiveEvent()
    final override val showItemsDialogEvents: SingleLiveEvent<ShowItemsDialogEventParameters> =
        SingleLiveEvent()
    final override val showErrorEvents: SingleLiveEvent<ShowErrorEventParameters> =
        SingleLiveEvent()
    final override val showSnackMessageEvents: SingleLiveEvent<ShowSnackMessageEventParameters> =
        SingleLiveEvent()
    final override val showAlertMessageEvents: SingleLiveEvent<ShowAlertMessageEventParameters> =
        SingleLiveEvent()
    final override val showToastEvents: SingleLiveEvent<ShowToastEventParameters> =
        SingleLiveEvent()

    final override val className: String = this::class.java.name
    final override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        val errorMessage = throwable.errorMessage
        additionalLogging(errorMessage)
        showErrorEvents.value =
            ShowErrorEventParameters(text = errorMessage)
    }

    override fun onCleared() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        channelsThatWillBeUnsubscribedInOnDestroy.forEach { it.cancel(cancellationException) }
        try {
            cancel(cancellationException)
        } catch (exception: Exception) {
            myLog(
                invokationPlace = methodName,
                text = "exception = $exception"
            )
        }

        super.onCleared()
    }

    protected val channelsThatWillBeUnsubscribedInOnDestroy: MutableList<ReceiveChannel<*>> =
        mutableListOf()

    protected fun showProgress() {
        showViewEvents.value = SHOW
    }

    protected fun hideProgress() {
        showViewEvents.value = HIDE
    }

    protected fun showToastMessage(
        showToastEventParameters: ShowToastEventParameters
    ) {
        showToastEvents.value = showToastEventParameters
    }

    protected fun showAlertMessage(
        showAlertMessageEvent: ShowAlertMessageEventParameters
    ) {
        showAlertMessageEvents.value = showAlertMessageEvent
    }

    protected fun showErrorAlert(
        showErrorEvent: ShowErrorEventParameters
    ) {
        showErrorEvents.value = showErrorEvent
    }

    protected fun showItemsDialog(
        showItemsDialogEvent: ShowItemsDialogEventParameters
    ) {
        showItemsDialogEvents.value = showItemsDialogEvent
    }

    protected fun showSnackMessage(showSnackMessageEventParameters: ShowSnackMessageEventParameters) {
        showSnackMessageEvents.value = showSnackMessageEventParameters
    }

}