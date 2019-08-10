package com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

interface EnterAuthenticationCodeUseCase {

    suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<Unit>

    suspend fun resendAuthenticationCodeCatching(): Result<Unit>

}
