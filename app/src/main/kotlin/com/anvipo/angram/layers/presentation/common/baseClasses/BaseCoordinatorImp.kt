package com.anvipo.angram.layers.presentation.common.baseClasses

import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.CoroutineExceptionHandlerWithLogger
import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.global.GlobalHelpers
import com.anvipo.angram.layers.presentation.common.interfaces.BaseCoordinator
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseCoordinatorImp<out CoordinateResultType>(
    private val systemMessageSendChannel: SystemMessageSendChannel
) :
    BaseCoordinator<CoordinateResultType>,
    CoroutineScope,
    HasLogger {

    final override val coroutineContext: CoroutineContext = Dispatchers.IO

    final override val className: String = this::class.java.name

    final override fun <T : Any> additionalLogging(logObj: T) {
        val coroutineExceptionHandlerWithLogger = CoroutineExceptionHandlerWithLogger { _, throwable ->
            onLogException(throwable)
        }

        launch(coroutineContext + coroutineExceptionHandlerWithLogger) {
            additionalLoggingHelper(logObj)
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    suspend fun <T> coordinateTo(
        coordinator: BaseCoordinatorImp<T>
    ): T {
        store(coordinator)

        val result = coordinator.start()

        free(coordinator)
        cancelAllJobs()

        return result
    }


    protected val childCoordinators: MutableMap<String, BaseCoordinatorImp<*>?> = mutableMapOf()

    protected open fun onLogException(throwable: Throwable): Unit = Unit

    protected open suspend fun <T> additionalLoggingHelper(logObj: T) {
        val systemMessage: SystemMessage = when (logObj) {
            null -> return
            is String -> GlobalHelpers.createTGSystemMessage(logObj)
            is SystemMessage -> logObj
            else -> {
                val text = "Undefined logObj type = $logObj"
                assertionFailure(text)
                TODO(text)
            }
        }

        systemMessageSendChannel.send(systemMessage)
    }

    protected fun showScreenHelper(
        exceptionHandler: (Throwable) -> Unit = { additionalLogging(it.localizedMessage) },
        showScreenBlock: suspend CoroutineScope.() -> Unit
    ) {
        myLaunch(
            context = Dispatchers.Main,
            block = showScreenBlock,
            exceptionHandler = exceptionHandler
        )
    }

    protected fun myLaunch(
        context: CoroutineContext = coroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        exceptionHandler: (Throwable) -> Unit = { additionalLogging(it.localizedMessage) },
        block: suspend CoroutineScope.() -> Unit
    ) {
        val coroutineExceptionHandlerWithLogger =
            CoroutineExceptionHandlerWithLogger { _, throwable ->
                exceptionHandler(throwable)
            }

        launch(
            context = context + coroutineExceptionHandlerWithLogger,
            start = start,
            block = block
        ).also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private val jobsThatWillBeCancelledInOnDestroy: MutableList<Job> = mutableListOf()

    private val uniqueIdentifier = UUID.randomUUID().toString()

    private fun cancelAllJobs() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        jobsThatWillBeCancelledInOnDestroy.forEach { it.cancel(cancellationException) }
    }

    private fun <T> store(coordinator: BaseCoordinatorImp<T>) {
        childCoordinators[coordinator.uniqueIdentifier] = coordinator
    }

    private fun <T> free(coordinator: BaseCoordinatorImp<T>) {
        childCoordinators.remove(coordinator.uniqueIdentifier)
    }

}