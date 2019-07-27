package com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase

import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import org.drinkless.td.libcore.telegram.TdApi

interface EnterAuthenticationCodeUseCase : BaseUseCase {

    suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<TdApi.Ok>

    suspend fun resendAuthenticationCodeCatching(): Result<TdApi.Ok>

}
