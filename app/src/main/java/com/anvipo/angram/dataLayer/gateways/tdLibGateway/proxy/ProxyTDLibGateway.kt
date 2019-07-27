package com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy

import org.drinkless.td.libcore.telegram.TdApi

interface ProxyTDLibGateway {

    suspend fun addProxyCatching(
        server: String,
        port: Int,
        enable: Boolean,
        type: TdApi.ProxyType
    ): Result<TdApi.Proxy>

}