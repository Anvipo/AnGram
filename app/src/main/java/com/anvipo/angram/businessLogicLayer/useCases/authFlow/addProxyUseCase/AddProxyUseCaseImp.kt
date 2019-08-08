package com.anvipo.angram.businessLogicLayer.useCases.authFlow.addProxyUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy.ProxyTDLibGateway
import org.drinkless.td.libcore.telegram.TdApi

class AddProxyUseCaseImp(
    private val tdLibGateway: ProxyTDLibGateway
) : AddProxyUseCase {

    override suspend fun addProxyCatching(
        server: String,
        port: Int,
        type: TdApi.ProxyType
    ): Result<Unit> =
        tdLibGateway
            .addProxyCatching(
                server = server,
                port = port,
                enable = true,
                type = type
            )
            .map {
                println()

                return@map
            }

}