package com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.base.BaseTdLibGatewayImp
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class ProxyTDLibGatewayImp(tdClient: Client) :
    BaseTdLibGatewayImp(tdClient),
    ProxyTDLibGateway {

    override suspend fun addProxyCatching(
        server: String,
        port: Int,
        enable: Boolean,
        type: TdApi.ProxyType
    ): Result<TdApi.Proxy> =
        doRequestCatching(
            TdApi.AddProxy(
                server,
                port,
                enable,
                type
            )
        )

}