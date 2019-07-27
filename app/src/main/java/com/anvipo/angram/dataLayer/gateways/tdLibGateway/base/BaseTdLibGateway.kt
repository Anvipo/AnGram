package com.anvipo.angram.dataLayer.gateways.tdLibGateway.base

import org.drinkless.td.libcore.telegram.TdApi

interface BaseTdLibGateway {

    suspend fun getAuthorizationStateRequestCatching(): Result<TdApi.AuthorizationState>

}