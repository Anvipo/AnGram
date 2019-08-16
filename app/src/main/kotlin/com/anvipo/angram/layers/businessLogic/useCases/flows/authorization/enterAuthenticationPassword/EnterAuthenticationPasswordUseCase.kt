package com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPassword

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordUseCase {

    suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<Unit>

}