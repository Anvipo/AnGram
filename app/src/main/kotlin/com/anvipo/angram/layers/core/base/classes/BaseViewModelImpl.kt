package com.anvipo.angram.layers.core.base.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.anvipo.angram.layers.core.events.parameters.ShowToastMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withContext
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
    final override val showConnectionSnackMessageEvents: LiveData<ShowSnackMessageEventParameters> by lazy {
        _showConnectionSnackMessageEvents
    }

    final override val showAlertMessageEvents: SingleLiveEvent<ShowAlertMessageEventParameters> =
        SingleLiveEvent()
    final override val showToastMessageEvents: SingleLiveEvent<ShowToastMessageEventParameters> =
        SingleLiveEvent()

    final override val className: String = this::class.java.name
    final override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        val errorMessage = throwable.errorMessage
        additionalLogging(errorMessage)

        myLaunch {
            showErrorAlert(
                ShowErrorEventParameters(
                    text = errorMessage,
                    cancelable = true,
                    messageDialogTag = null
                )
            )
        }
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

    protected suspend fun showProgress() {
        withContext(Dispatchers.Main) {
            showViewEvents.value = SHOW
        }
    }

    protected suspend fun hideProgress() {
        withContext(Dispatchers.Main) {
            showViewEvents.value = HIDE
        }
    }

    protected suspend fun showToastMessage(
        showToastMessageEventParameters: ShowToastMessageEventParameters
    ) {
        withContext(Dispatchers.Main) {
            showToastMessageEvents.value = showToastMessageEventParameters
        }
    }

    protected suspend fun showAlertMessage(
        showAlertMessageEvent: ShowAlertMessageEventParameters
    ) {
        withContext(Dispatchers.Main) {
            showAlertMessageEvents.value = showAlertMessageEvent
        }
    }

    protected suspend fun showErrorAlert(
        showErrorEvent: ShowErrorEventParameters
    ) {
        withContext(Dispatchers.Main) {
            showErrorEvents.value = showErrorEvent
        }
    }

    protected suspend fun showItemsDialog(
        showItemsDialogEvent: ShowItemsDialogEventParameters
    ) {
        withContext(Dispatchers.Main) {
            showItemsDialogEvents.value = showItemsDialogEvent
        }
    }

    protected suspend fun showSnackMessage(
        showSnackMessageEventParameters: ShowSnackMessageEventParameters
    ) {
        withContext(Dispatchers.Main) {
            showSnackMessageEvents.value = showSnackMessageEventParameters
        }
    }

    protected suspend fun showConnectionSnackMessage(
        showSnackMessageEventParameters: ShowSnackMessageEventParameters
    ) {
        withContext(Dispatchers.Main) {
            _showConnectionSnackMessageEvents.value = showSnackMessageEventParameters
        }
    }

    private val _showConnectionSnackMessageEvents =
        MutableLiveData<ShowSnackMessageEventParameters>()

}