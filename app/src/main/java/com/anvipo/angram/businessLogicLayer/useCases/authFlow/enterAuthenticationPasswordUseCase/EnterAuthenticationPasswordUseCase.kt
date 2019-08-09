package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPasswordUseCase

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordUseCase {

    suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<Unit>

}