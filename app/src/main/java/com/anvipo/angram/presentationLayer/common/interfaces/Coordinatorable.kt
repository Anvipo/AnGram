package com.anvipo.angram.presentationLayer.common.interfaces

interface Coordinatorable {

    var finishFlow: (() -> Unit)?

    fun start()

}