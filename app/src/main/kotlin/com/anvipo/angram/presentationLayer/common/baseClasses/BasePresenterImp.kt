package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.coreLayer.base.interfaces.BaseView
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
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
    CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    override fun onPauseTriggered() {
        for (channel in channelsThatWillBeUnsubscribedInOnPause) {
            channel.cancel(CancellationException("BasePresenterImp onPauseTriggered"))
        }
    }

    override fun onDestroy() {
        for (job in jobsThatWillBeCancelledInOnDestroy) {
            job.cancel()
        }
        super.onDestroy()
    }

    protected val jobsThatWillBeCancelledInOnDestroy: MutableList<Job> = mutableListOf()
    protected val channelsThatWillBeUnsubscribedInOnPause: MutableList<ReceiveChannel<*>> = mutableListOf()

}