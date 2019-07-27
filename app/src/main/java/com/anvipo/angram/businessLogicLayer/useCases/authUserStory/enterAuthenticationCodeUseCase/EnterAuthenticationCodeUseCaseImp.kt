package com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationCodeUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

class EnterAuthenticationCodeUseCaseImp(
    private val tdLibGateway: AuthorizationTDLibGateway
) : EnterAuthenticationCodeUseCase {

    override suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<Unit> = tdLibGateway
        .checkAuthenticationCodeCatching(
            enteredAuthenticationCode,
            lastName,
            firstName
        )
        .map {}

    override suspend fun resendAuthenticationCodeCatching(): Result<Unit> =
        tdLibGateway
            .resendAuthenticationCodeCatching()
            .map {}

}