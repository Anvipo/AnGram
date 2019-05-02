package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable

abstract class BaseCoordinator : Coordinatorable {

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

}