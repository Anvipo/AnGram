package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPassword

import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
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