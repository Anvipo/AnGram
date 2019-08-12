package com.anvipo.angram.layers.presentation.common.baseClasses

import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter
import com.anvipo.angram.layers.presentation.common.interfaces.HasMyCoroutineBuilders
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

abstract class BasePresenterImp<V : BaseView> :
    MvpPresenter<V>(),
    BasePresenter,
    HasMyCoroutineBuilders,
    HasLogger {

    final override val coroutineContext: CoroutineContext = Dispatchers.IO

    final override val className: String = this::class.java.name

    final override fun onDestroy() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        channelsThatWillBeUnsubscribedInOnDestroy.forEach { it.cancel(cancellationException) }
        cancel(cancellationException)
        super.onDestroy()
    }

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        val errorMessage = throwable.errorMessage
        additionalLogging(errorMessage)
        viewState.showErrorAlert(errorMessage)
    }

    protected val channelsThatWillBeUnsubscribedInOnDestroy: MutableList<ReceiveChannel<*>> = mutableListOf()

}