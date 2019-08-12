package com.anvipo.angram.layers.data.gateways.tdLib.proxy

import com.anvipo.angram.layers.data.gateways.tdLib.base.BaseTdLibGateway
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class ProxyTDLibGatewayImp(tdLibClient: Client) :
    BaseTdLibGateway(tdLibClient),
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

    override suspend fun pingProxyCatching(proxyId: Int): Result<TdApi.Seconds> =
        doRequestCatching(
            TdApi.PingProxy(
                proxyId
            )
        )

}