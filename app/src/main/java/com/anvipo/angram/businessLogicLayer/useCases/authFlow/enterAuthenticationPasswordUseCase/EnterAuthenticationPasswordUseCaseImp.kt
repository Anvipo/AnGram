package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPasswordUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

class EnterAuthenticationPasswordUseCaseImp(
    private val tdLibGateway: AuthorizationTDLibGateway
) : EnterAuthenticationPasswordUseCase {

    override suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<Unit> =
        tdLibGateway
            .checkAuthenticationPasswordCatching(enteredAuthenticationPassword)
            .map {}

}