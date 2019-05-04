package com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway

import android.content.Context
import org.drinkless.td.libcore.telegram.TdApi

@Suppress("DirectUseOfResultType")
interface TDLibGateway {

    suspend fun getAuthorizationStateRequestCatching(): Result<TdApi.AuthorizationState>

    suspend fun setTdLibParametersCatching(context: Context): Result<TdApi.Ok>

    suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok>

    suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok>

}
