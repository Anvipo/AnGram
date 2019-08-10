package com.anvipo.angram.presentationLayer.common.interfaces

interface BaseCoordinator<out CoordinateResultType> : Coordinatorable {

    suspend fun start(): CoordinateResultType

}