package com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationCode

import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

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