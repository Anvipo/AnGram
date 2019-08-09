package com.anvipo.angram.presentationLayer.common.interfaces

interface BaseCoordinator : Coordinatorable {

    var finishFlow: (() -> Unit)?

    fun coldStart()
    fun hotStart(): Unit = Unit

}