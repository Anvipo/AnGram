package com.anvipo.angram.layers.core.logHelpers

import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import com.anvipo.angram.layers.core.errorMessage
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
        logIfShould(
            text = exception.errorMessage,
            invokationPlace = exception.stackTrace.first().className
        )
        handler?.invoke(context, exception)
    }

}