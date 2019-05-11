package com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import org.drinkless.td.libcore.telegram.TdApi

@Suppress("DirectUseOfResultType")
class EnterAuthenticationCodeUseCaseImp(
    private val tdLibGateway: TDLibGateway
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