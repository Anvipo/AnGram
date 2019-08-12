package com.anvipo.angram.layers.businessLogic.useCases.app

interface AppUseCase {

    suspend fun saveEnabledProxyId(enabledProxyId: Int?)

}