package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationCodeUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

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