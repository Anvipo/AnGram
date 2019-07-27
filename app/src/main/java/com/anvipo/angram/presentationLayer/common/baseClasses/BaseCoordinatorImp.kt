package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.presentationLayer.common.interfaces.BaseCoordinator
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import kotlinx.coroutines.CoroutineScope

abstract class BaseCoordinatorImp : BaseCoordinator, CoroutineScope {

    protected val childCoordinators: MutableList<Coordinatorable> = mutableListOf()

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

    protected abstract fun cancelAllJobs()

    protected fun onFinishFlowWrapper() {
        cancelAllJobs()
        finishFlow?.invoke()
    }

}