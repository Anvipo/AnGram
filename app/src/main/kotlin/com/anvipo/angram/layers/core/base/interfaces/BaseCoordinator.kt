package com.anvipo.angram.layers.core.base.interfaces

interface BaseCoordinator<out CoordinateResultType> : Coordinatorable {

    suspend fun start(): CoordinateResultType

    fun freeAllResources()

}