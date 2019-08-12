package com.anvipo.angram.layers.presentation.common.baseClasses

import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.presentation.common.interfaces.BaseCoordinator
import com.anvipo.angram.layers.presentation.common.interfaces.Coordinatorable
import com.anvipo.angram.layers.presentation.common.interfaces.HasMyCoroutineBuilders
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

abstract class BaseCoordinatorImp<CoordinateResultType>(
    private val systemMessageSendChannel: SystemMessageSendChannel
) :
    BaseCoordinator<CoordinateResultType>,
    HasMyCoroutineBuilders,
    HasLogger {

    final override val coroutineContext: CoroutineContext = Dispatchers.IO

    final override val className: String = this::class.java.name

    final override fun <T : Any> additionalLogging(logObj: T) {
        val systemMessage: SystemMessage = when (logObj) {
            is String -> createTGSystemMessage(logObj)
            is SystemMessage -> logObj
            else -> {
                val text = "Undefined logObj type = $logObj"
                assertionFailure(text)
                TODO(text)
            }
        }

        val couldImmediatelySend = systemMessageSendChannel.offer(systemMessage)

        if (!couldImmediatelySend) {
            logIfShould(
                invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                text = "couldImmediatelySend = $couldImmediatelySend"
            )
        }
    }

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        additionalLogging(throwable.errorMessage)
    }

    suspend fun <T> coordinateTo(
        coordinator: BaseCoordinator<T>
    ): T {
        store(coordinator)

        val result = coordinator.start()

        free(coordinator)
        cancelAllJobs()

        return result
    }


    protected val childCoordinators: MutableList<Coordinatorable> = mutableListOf()

    protected lateinit var finishFlowContinuation: Continuation<CoordinateResultType>

    protected open fun onLogException(throwable: Throwable): Unit = Unit


    private fun cancelAllJobs() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        cancel(cancellationException)
    }

    private fun store(coordinator: Coordinatorable) {
        childCoordinators.add(coordinator)
    }

    private fun free(coordinator: Coordinatorable) {
        childCoordinators.remove(coordinator)
    }

}