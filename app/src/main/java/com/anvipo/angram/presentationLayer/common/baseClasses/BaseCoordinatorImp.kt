package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.presentationLayer.common.interfaces.BaseCoordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseCoordinatorImp<out CoordinateResultType> :
    BaseCoordinator<CoordinateResultType>,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    suspend fun <T> coordinateTo(
        coordinator: BaseCoordinatorImp<T>
    ): T {
        store(coordinator)

        val result = coordinator.start()

        free(coordinator)
        cancelAllJobs()

        return result
    }


    protected val jobsThatWillBeCancelledInOnDestroy: MutableList<Job> = mutableListOf()

    protected val childCoordinators: MutableMap<String, BaseCoordinatorImp<*>?> = mutableMapOf()

    private val uniqueIdentifier = UUID.randomUUID().toString()

    private fun cancelAllJobs() {
        for (job in jobsThatWillBeCancelledInOnDestroy) {
            job.cancel()
        }
    }

    private fun <T> store(coordinator: BaseCoordinatorImp<T>) {
        childCoordinators[coordinator.uniqueIdentifier] = coordinator
    }

    private fun <T> free(coordinator: BaseCoordinatorImp<T>) {
        childCoordinators.remove(coordinator.uniqueIdentifier)
    }

}