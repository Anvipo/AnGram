package com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationPasswordUseCase

import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import org.drinkless.td.libcore.telegram.TdApi

interface EnterAuthenticationPasswordUseCase : BaseUseCase {
    @Suppress("DirectUseOfResultType")
    suspend fun checkAuthenticationPasswordCatching(enteredAuthenticationPassword: CorrectAuthenticationPasswordType): Result<TdApi.Ok>
}