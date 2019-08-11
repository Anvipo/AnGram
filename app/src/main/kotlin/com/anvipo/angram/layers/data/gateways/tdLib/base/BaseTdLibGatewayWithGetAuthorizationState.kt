package com.anvipo.angram.layers.data.gateways.tdLib.base

import org.drinkless.td.libcore.telegram.TdApi

interface BaseTdLibGatewayWithGetAuthorizationState : BaseTdLibGateway {

    suspend fun getAuthorizationStateCatching(): Result<TdApi.AuthorizationState>

}