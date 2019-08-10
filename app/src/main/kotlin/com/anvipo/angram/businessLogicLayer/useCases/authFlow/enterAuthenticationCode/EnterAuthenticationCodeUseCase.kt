package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationCode

import com.anvipo.angram.presentationLayer.flows.auth.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

interface EnterAuthenticationCodeUseCase {

    suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<Unit>

    suspend fun resendAuthenticationCodeCatching(): Result<Unit>

}
