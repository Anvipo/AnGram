package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.presentationLayer.common.interfaces.BaseCoordinator
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseCoordinatorImp : BaseCoordinator, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default


    protected val childCoordinators: MutableList<Coordinatorable> = mutableListOf()

    protected val jobsThatWillBeCancelledInOnDestroy: MutableList<Job> = mutableListOf()

    protected fun addChildCoordinator(coordinator: Coordinatorable) {
        for (element in childCoordinators) {
            if (element === coordinator) {
                return
            }
        }

        childCoordinators.add(coordinator)
    }

    protected fun removeChildCoordinator(coordinator: Coordinatorable) {
        if (childCoordinators.isEmpty()) {
            return
        }

        for ((index, element) in childCoordinators.withIndex()) {
            if (element === coordinator) {
                childCoordinators.removeAt(index)
            }
        }
    }

    protected fun onFinishFlowWrapper() {
        for (job in jobsThatWillBeCancelledInOnDestroy) {
            job.cancel()
        }
        finishFlow?.invoke()
    }

}