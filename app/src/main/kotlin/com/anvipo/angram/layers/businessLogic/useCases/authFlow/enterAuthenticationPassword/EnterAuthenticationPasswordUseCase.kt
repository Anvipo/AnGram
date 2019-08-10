package com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword

import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordUseCase {

    suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<Unit>

}