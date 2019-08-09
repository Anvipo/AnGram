package com.anvipo.angram.businessLogicLayer.useCases.app

import org.drinkless.td.libcore.telegram.TdApi

interface AppUseCase {

    suspend fun pingEnabledProxy(): Result<Double>

    suspend fun setNetworkType(networkType: TdApi.NetworkType): Result<Unit>

    suspend fun saveEnabledProxyId(enabledProxyId: Int)

}