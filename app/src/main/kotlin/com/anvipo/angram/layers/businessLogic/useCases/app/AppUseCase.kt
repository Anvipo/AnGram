package com.anvipo.angram.layers.businessLogic.useCases.app

import com.anvipo.angram.layers.core.NetworkConnectionState

interface AppUseCase {

    suspend fun saveEnabledProxyId(enabledProxyId: Int?)

    suspend fun onChangeNetworkConnectionState(newState: NetworkConnectionState): Result<Unit>

}