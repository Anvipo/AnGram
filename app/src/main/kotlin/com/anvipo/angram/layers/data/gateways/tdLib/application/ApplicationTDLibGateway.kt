package com.anvipo.angram.layers.data.gateways.tdLib.application

import com.anvipo.angram.layers.data.gateways.tdLib.base.BaseTdLibGatewayWithGetAuthorizationState
import org.drinkless.td.libcore.telegram.TdApi

interface ApplicationTDLibGateway : BaseTdLibGatewayWithGetAuthorizationState {

    suspend fun setTdLibParametersCatching(): Result<TdApi.Ok>

    suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok>

    suspend fun logoutCatching(): Result<TdApi.Ok>

}