package com.anvipo.angram.layers.presentation.common.baseClasses

import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter
import com.anvipo.angram.layers.presentation.common.interfaces.HasMyCoroutineBuilders
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

abstract class BasePresenterImp<V : BaseView> :
    MvpPresenter<V>(),
    BasePresenter,
    HasMyCoroutineBuilders,
    HasLogger {

    final override val coroutineContext: CoroutineContext = Dispatchers.IO

    final override val className: String = this::class.java.name

    final override val jobsThatMustBeCancelledInLifecycleEnd: MutableList<Job> = mutableListOf()

    override fun onPauseTriggered() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        channelsThatWillBeUnsubscribedInOnPause.forEach { it.cancel(cancellationException) }
    }

    final override fun onDestroy() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        jobsThatMustBeCancelledInLifecycleEnd.forEach { it.cancel(cancellationException) }
        super.onDestroy()
    }

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        val localizedMessage = throwable.localizedMessage
        additionalLogging(localizedMessage)
        viewState.showErrorAlert(localizedMessage)
    }

    protected val channelsThatWillBeUnsubscribedInOnPause: MutableList<ReceiveChannel<*>> = mutableListOf()

}