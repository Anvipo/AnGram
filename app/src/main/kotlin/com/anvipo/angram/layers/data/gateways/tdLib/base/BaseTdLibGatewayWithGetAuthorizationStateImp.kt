package com.anvipo.angram.layers.data.gateways.tdLib.base

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

abstract class BaseTdLibGatewayWithGetAuthorizationStateImp(
    tdClient: Client
) : BaseTdLibGatewayImp(tdClient), BaseTdLibGatewayWithGetAuthorizationState {

    final override suspend fun getAuthorizationStateCatching(): Result<TdApi.AuthorizationState> =
        doRequestCatching(TdApi.GetAuthorizationState())

}