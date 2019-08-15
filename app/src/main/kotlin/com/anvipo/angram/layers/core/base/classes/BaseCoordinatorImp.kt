package com.anvipo.angram.layers.core.base.classes

import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import com.anvipo.angram.layers.core.UiScope
import com.anvipo.angram.layers.core.base.interfaces.BaseCoordinator
import com.anvipo.angram.layers.core.base.interfaces.Coordinatorable
import com.anvipo.angram.layers.core.base.interfaces.HasMyCoroutineBuilders
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import kotlinx.coroutines.*
import kotlin.coroutines.Continuation

abstract class BaseCoordinatorImp<CoordinateResultType>(
    private val systemMessageSendChannel: SystemMessageSendChannel
) :
    BaseCoordinator<CoordinateResultType>,
    HasMyCoroutineBuilders,
    HasLogger,
    CoroutineScope by UiScope() {

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
        withContext(Dispatchers.Default) {
            store(coordinator)
        }

        val coordinateResult = withContext(Dispatchers.Main) { coordinator.start() }

        withContext(Dispatchers.Default) {
            free(coordinator)
        }

        cancelAllJobs()

        return coordinateResult
    }


    protected lateinit var finishFlowContinuation: Continuation<CoordinateResultType>

    protected open fun onLogException(throwable: Throwable): Unit = Unit

    protected fun clearChildCoordinators() {
        childCoordinators.clear()
    }

    private val childCoordinators: MutableList<Coordinatorable> = mutableListOf()


    private fun cancelAllJobs() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        try {
            cancel(cancellationException)
        } catch (exception: Exception) {
            myLog(
                invokationPlace = methodName,
                text = "exception = $exception"
            )
        }
    }

    private fun store(coordinator: Coordinatorable) {
        childCoordinators.add(coordinator)
    }

    private fun free(coordinator: Coordinatorable) {
        childCoordinators.remove(coordinator)
    }

}