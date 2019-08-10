package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPassword

import com.anvipo.angram.presentationLayer.flows.auth.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordUseCase {

    suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<Unit>

}