package com.anvipo.angram.layers.data.gateways.tdLib.authorization

import com.anvipo.angram.layers.data.gateways.tdLib.base.BaseTdLibGateway
import com.anvipo.angram.presentationLayer.flows.auth.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.presentationLayer.flows.auth.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import org.drinkless.td.libcore.telegram.TdApi

interface AuthorizationTDLibGateway : BaseTdLibGateway {

    suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok>

    suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<TdApi.Ok>

    suspend fun resendAuthenticationCodeCatching(): Result<TdApi.Ok>

    suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<TdApi.Ok>

}
