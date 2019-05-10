package com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway

import android.content.Context
import com.anvipo.angram.businessLogicLayer.gateways.base.BaseGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType
import org.drinkless.td.libcore.telegram.TdApi

@Suppress("DirectUseOfResultType")
interface TDLibGateway : BaseGateway {

    suspend fun getAuthorizationStateRequestCatching(): Result<TdApi.AuthorizationState>

    suspend fun setTdLibParametersCatching(context: Context): Result<TdApi.Ok>

    suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok>

    suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok>

    suspend fun checkAuthenticationCodeCatching(
        enteredAuthCode: CorrectAuthCodeType,
        lastName: String,
        firstName: String
    ): Result<TdApi.Ok>

}
