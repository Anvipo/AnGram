package com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.base.BaseTdLibGateway
import org.drinkless.td.libcore.telegram.TdApi

interface ProxyTDLibGateway : BaseTdLibGateway {

    suspend fun addProxyCatching(
        server: String,
        port: Int,
        enable: Boolean,
        type: TdApi.ProxyType
    ): Result<TdApi.Proxy>

    suspend fun pingProxyCatching(proxyId: Int): Result<TdApi.Seconds>

}