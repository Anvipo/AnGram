package com.anvipo.angram.layers.businessLogic.useCases.app

import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAO

class AppUseCaseImp(
    private val sharedPreferencesGateway: SharedPreferencesDAO
) : AppUseCase {

    override suspend fun saveEnabledProxyId(enabledProxyId: Int?) {
        sharedPreferencesGateway.saveEnabledProxyId(enabledProxyId)
    }

}