package com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import org.drinkless.td.libcore.telegram.TdApi

class EnterAuthenticationCodeUseCaseImp(
    private val tdLibGateway: AuthorizationTDLibGateway
) : EnterAuthenticationCodeUseCase {

    override suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<TdApi.Ok> = tdLibGateway.checkAuthenticationCodeCatching(
        enteredAuthenticationCode,
        lastName = lastName,
        firstName = firstName
    )

    override suspend fun resendAuthenticationCodeCatching(): Result<TdApi.Ok> =
        tdLibGateway.resendAuthenticationCodeCatching()

}