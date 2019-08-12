package com.anvipo.angram.layers.presentation.common.interfaces

import com.anvipo.angram.layers.core.CoroutineExceptionHandlerWithLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface HasMyCoroutineBuilders : CoroutineScope {

    fun myLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        customMyLaunchExceptionHandler: ((Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val coroutineExceptionHandlerWithLogger =
            CoroutineExceptionHandlerWithLogger { _, throwable ->
                customMyLaunchExceptionHandler?.invoke(throwable)
                    ?: myLaunchExceptionHandler(throwable)
            }

        return launch(
            context = context + coroutineExceptionHandlerWithLogger,
            start = start,
            block = block
        )
    }

    fun myLaunchExceptionHandler(throwable: Throwable)

}