package com.anvipo.angram.dataLayer.gateways.tdLibGateway.base

import org.drinkless.td.libcore.telegram.TdApi

interface BaseTdLibGateway {

    suspend fun getAuthorizationStateRequestCatching(): Result<TdApi.AuthorizationState>

    suspend fun setNetworkTypeCatching(type: TdApi.NetworkType): Result<TdApi.Ok>

}