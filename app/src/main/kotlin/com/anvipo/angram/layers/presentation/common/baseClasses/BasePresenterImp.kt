package com.anvipo.angram.layers.presentation.common.baseClasses

import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

abstract class BasePresenterImp<V : BaseView> :
    MvpPresenter<V>(),
    BasePresenter,
    CoroutineScope,
    HasLogger {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    override val className: String = this::class.java.name

    override fun onPauseTriggered() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        channelsThatWillBeUnsubscribedInOnPause.forEach { it.cancel(cancellationException) }
    }

    override fun onDestroy() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        jobsThatWillBeCancelledInOnDestroy.forEach { it.cancel(cancellationException) }
        super.onDestroy()
    }

    protected val jobsThatWillBeCancelledInOnDestroy: MutableList<Job> = mutableListOf()
    protected val channelsThatWillBeUnsubscribedInOnPause: MutableList<ReceiveChannel<*>> = mutableListOf()

}