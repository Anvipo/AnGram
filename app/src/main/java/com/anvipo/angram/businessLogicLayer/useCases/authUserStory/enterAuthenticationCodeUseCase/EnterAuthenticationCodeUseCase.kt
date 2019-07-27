package com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationCodeUseCase

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

interface EnterAuthenticationCodeUseCase {

    suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<Unit>

    suspend fun resendAuthenticationCodeCatching(): Result<Unit>

}
