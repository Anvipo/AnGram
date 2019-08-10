package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationCode

import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.auth.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

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