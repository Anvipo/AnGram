package com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

interface EnterAuthenticationCodeUseCase {

    suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<Unit>

    suspend fun resendAuthenticationCodeCatching(): Result<Unit>

}
