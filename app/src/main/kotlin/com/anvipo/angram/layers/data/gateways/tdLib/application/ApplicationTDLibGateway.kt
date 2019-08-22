package com.anvipo.angram.layers.data.gateways.tdLib.application

import com.anvipo.angram.layers.core.NetworkConnectionState
import org.drinkless.td.libcore.telegram.TdApi

interface ApplicationTDLibGateway {

    suspend fun setTdLibParametersCatching(): Result<TdApi.Ok>

    suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok>

    suspend fun logoutCatching(): Result<TdApi.Ok>

    suspend fun onChangeNetworkConnectionState(newState: NetworkConnectionState): Result<TdApi.Ok>

}