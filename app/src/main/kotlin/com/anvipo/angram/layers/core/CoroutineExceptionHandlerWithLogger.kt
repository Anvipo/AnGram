package com.anvipo.angram.layers.core

import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class CoroutineExceptionHandlerWithLogger(
    private val handler: ((CoroutineContext, Throwable) -> Unit)? = null
) : CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler.Key

    override fun handleException(
        context: CoroutineContext,
        exception: Throwable
    ) {
        logIfShould(exception.localizedMessage)
        handler?.invoke(context, exception)
    }

}