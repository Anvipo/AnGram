package com.anvipo.angram.presentationLayer.common.interfaces

interface CoordinatorInput : Coordinatorable {

    var finishFlow: (() -> Unit)?

    fun coldStart()
    fun hotStart() {}

}