package com.anvipo.angram.dataLayer.gateways.tdLib.authorization

import com.anvipo.angram.dataLayer.gateways.tdLib.base.BaseTdLibGatewayImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class AuthorizationTDLibGatewayImp(tdClient: Client) : BaseTdLibGatewayImp(tdClient),
    AuthorizationTDLibGateway {

    override suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok> =
        doRequestCatching(
            TdApi.SetAuthenticationPhoneNumber(
                enteredPhoneNumber,
                true,
                true
            )
        )

    override suspend fun resendAuthenticationCodeCatching(): Result<TdApi.Ok> =
        doRequestCatching(TdApi.ResendAuthenticationCode())

    override suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<TdApi.Ok> =
        doRequestCatching(
            TdApi.CheckAuthenticationCode(
                enteredAuthenticationCode,
                firstName,
                lastName
            )
        )

    override suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<TdApi.Ok> =
        doRequestCatching(TdApi.CheckAuthenticationPassword(enteredAuthenticationPassword))

}
