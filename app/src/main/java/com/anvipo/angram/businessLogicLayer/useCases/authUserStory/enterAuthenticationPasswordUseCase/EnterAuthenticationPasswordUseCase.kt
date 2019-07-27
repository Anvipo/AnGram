package com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationPasswordUseCase

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordUseCase {

    suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<Unit>

}