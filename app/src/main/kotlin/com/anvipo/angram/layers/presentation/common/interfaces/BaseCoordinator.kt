package com.anvipo.angram.layers.presentation.common.interfaces

interface BaseCoordinator<out CoordinateResultType> : Coordinatorable {

    suspend fun start(): CoordinateResultType

}