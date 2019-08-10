package com.anvipo.angram.businessLogicLayer.useCases.app

import com.anvipo.angram.dataLayer.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.dataLayer.gateways.tdLib.proxy.ProxyTDLibGateway
import org.drinkless.td.libcore.telegram.TdApi

class AppUseCaseImp(
    private val proxyTDLibGateway: ProxyTDLibGateway,
    private val sharedPreferencesGateway: SharedPreferencesDAO
) : AppUseCase {

    override suspend fun pingEnabledProxy(): Result<Double> {
        val enabledProxyId =
            sharedPreferencesGateway
                .getEnabledProxyId() ?: return Result.failure(Error("enabledProxyId == null"))

        return proxyTDLibGateway
            .pingProxyCatching(enabledProxyId)
            .map { it.seconds }
    }

    override suspend fun setNetworkType(networkType: TdApi.NetworkType): Result<Unit> =
        proxyTDLibGateway
            .setNetworkTypeCatching(networkType)
            .map {}

    override suspend fun saveEnabledProxyId(enabledProxyId: Int?) {
        sharedPreferencesGateway.saveEnabledProxyId(enabledProxyId)
    }

}