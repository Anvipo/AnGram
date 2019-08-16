package com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword

import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

class EnterAuthenticationPasswordUseCaseImpl(
    private val tdLibGateway: AuthorizationTDLibGateway
) : EnterAuthenticationPasswordUseCase {

    override suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<Unit> =
        tdLibGateway
            .checkAuthenticationPasswordCatching(enteredAuthenticationPassword)
            .map {}

}