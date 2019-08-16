package com.anvipo.angram.layers.core.base.classes

import com.anvipo.angram.layers.core.UiScope
import com.anvipo.angram.layers.core.base.interfaces.BasePresenter
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.core.base.interfaces.HasMyCoroutineBuilders
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel

abstract class BasePresenterImp<V : BaseView> :
    MvpPresenter<V>(),
    BasePresenter,
    HasMyCoroutineBuilders,
    CoroutineScope by UiScope(),
    HasLogger {

    final override val className: String = this::class.java.name

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        val errorMessage = throwable.errorMessage
        additionalLogging(errorMessage)
        viewState.showErrorAlert(errorMessage)
    }

    final override fun onDestroy() {
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
        super.onDestroy()
    }

    protected val channelsThatWillBeUnsubscribedInOnDestroy: MutableList<ReceiveChannel<*>> = mutableListOf()

}