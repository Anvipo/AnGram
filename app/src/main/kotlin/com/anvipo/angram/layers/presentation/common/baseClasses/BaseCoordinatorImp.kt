package com.anvipo.angram.layers.presentation.common.baseClasses

import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.global.GlobalHelpers
import com.anvipo.angram.layers.presentation.common.interfaces.BaseCoordinator
import com.anvipo.angram.layers.presentation.common.interfaces.HasMyCoroutineBuilders
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseCoordinatorImp<out CoordinateResultType>(
    private val systemMessageSendChannel: SystemMessageSendChannel
) :
    BaseCoordinator<CoordinateResultType>,
    HasMyCoroutineBuilders,
    HasLogger {

    final override val coroutineContext: CoroutineContext = Dispatchers.IO

    final override val className: String = this::class.java.name

    final override val jobsThatMustBeCancelledInLifecycleEnd: MutableList<Job> = mutableListOf()

    final override fun <T : Any> additionalLogging(logObj: T) {
        myLaunch(
            customMyLaunchExceptionHandler = ::onLogException
        ) {
            additionalLoggingHelper(logObj)
        }
    }

    final override fun myLaunchExceptionHandler(throwable: Throwable) {
        additionalLogging(throwable.localizedMessage)
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


    private val uniqueIdentifier = UUID.randomUUID().toString()

    private fun cancelAllJobs() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        jobsThatMustBeCancelledInLifecycleEnd.forEach { it.cancel(cancellationException) }
    }

    private fun <T> store(coordinator: BaseCoordinatorImp<T>) {
        childCoordinators[coordinator.uniqueIdentifier] = coordinator
    }

    private fun <T> free(coordinator: BaseCoordinatorImp<T>) {
        childCoordinators.remove(coordinator.uniqueIdentifier)
    }

}