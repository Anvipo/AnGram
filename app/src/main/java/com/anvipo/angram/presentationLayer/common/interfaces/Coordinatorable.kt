package com.anvipo.angram.presentationLayer.common.interfaces

interface Coordinatorable {

    // TODO: coroutine context and error handler

    fun coldStart()
    fun hotStart() {}

}