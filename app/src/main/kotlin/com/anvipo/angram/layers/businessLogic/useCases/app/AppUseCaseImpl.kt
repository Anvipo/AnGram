package com.anvipo.angram.layers.businessLogic.useCases.app

import com.anvipo.angram.layers.core.NetworkConnectionState
import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway

class AppUseCaseImpl(
    private val sharedPreferencesGateway: SharedPreferencesDAO,
    private val gateway: ApplicationTDLibGateway
) : AppUseCase {

    override suspend fun onChangeNetworkConnectionState(newState: NetworkConnectionState): Result<Unit> =
        gateway.onChangeNetworkConnectionState(newState).map {}

    override suspend fun saveEnabledProxyId(enabledProxyId: Int?) {
        sharedPreferencesGateway.saveEnabledProxyId(enabledProxyId)
    }

}