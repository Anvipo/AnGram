package com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.addProxy

import org.drinkless.td.libcore.telegram.TdApi

interface AddProxyUseCase {

    suspend fun addProxyCatching(
        server: String,
        port: Int,
        type: TdApi.ProxyType
    ): Result<Unit>

}