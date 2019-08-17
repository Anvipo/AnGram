package com.anvipo.angram.layers.core.base.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anvipo.angram.layers.core.ShowItemsDialogEvent
import com.anvipo.angram.layers.core.ShowSnackMessageEvent
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.base.interfaces.HasMyCoroutineBuilders
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.events.ShowAlertMessageEvent
import com.anvipo.angram.layers.core.events.ShowErrorEvent
import com.anvipo.angram.layers.core.events.ShowViewEvent
import com.anvipo.angram.layers.core.events.ShowViewEvent.HIDE
import com.anvipo.angram.layers.core.events.ShowViewEvent.SHOW
import com.anvipo.angram.layers.core.events.SingleLiveEvent
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

    final override val showErrorEvents: SingleLiveEvent<ShowErrorEvent> =
        SingleLiveEvent()
    final override val showViewEvents: SingleLiveEvent<ShowViewEvent> =
        SingleLiveEvent()
    final override val showSnackMessageEvents: SingleLiveEvent<ShowSnackMessageEvent> =
        SingleLiveEvent()
    final override val showItemsDialogEvents: SingleLiveEvent<ShowItemsDialogEvent> =
        SingleLiveEvent()
    final override val showAlertMessageEvents: SingleLiveEvent<ShowAlertMessageEvent> =
        SingleLiveEvent()
    final override val className: String = this::class.java.name
    final override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        val errorMessage = throwable.errorMessage
        additionalLogging(errorMessage)
        showErrorEvents.value = ShowErrorEvent(text = errorMessage)
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

    protected fun showAlertMessage(showAlertMessageEvent: ShowAlertMessageEvent) {
        showAlertMessageEvents.value = showAlertMessageEvent
    }

    protected fun showErrorAlert(showErrorEvent: ShowErrorEvent) {
        showErrorEvents.value = showErrorEvent
    }

    protected fun showItemsDialog(
        showItemsDialogEvent: ShowItemsDialogEvent
    ) {
        showItemsDialogEvents.value = showItemsDialogEvent
    }

    protected fun showSnackMessage(text: String) {
        showSnackMessageEvents.value = ShowSnackMessageEvent(
            text = text
        )
    }

}