package com.anvipo.angram.layers.presentation.common.interfaces

import com.anvipo.angram.layers.core.CoroutineExceptionHandlerWithLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface HasMyCoroutineBuilders : CoroutineScope {

    val jobsThatMustBeCancelledInLifecycleEnd: MutableList<Job>

    fun myLaunch(
        context: CoroutineContext = coroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        customMyLaunchExceptionHandler: ((Throwable) -> Unit)? = null,
        customAppendMyLaunchJob: ((Job) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val coroutineExceptionHandlerWithLogger =
            CoroutineExceptionHandlerWithLogger { _, throwable ->
                customMyLaunchExceptionHandler?.invoke(throwable) ?: myLaunchExceptionHandler(throwable)
            }

        launch(
            context = context + coroutineExceptionHandlerWithLogger,
            start = start,
            block = block
        ).also {
            customAppendMyLaunchJob?.invoke(it) ?: appendMyLaunchJobToJobsThatMustBeCancelledInLifecycleEnd(it)
        }
    }

    fun appendMyLaunchJobToJobsThatMustBeCancelledInLifecycleEnd(newMyLaunchJob: Job) {
        jobsThatMustBeCancelledInLifecycleEnd += newMyLaunchJob
    }

    fun myLaunchExceptionHandler(throwable: Throwable)

}