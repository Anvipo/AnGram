package com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase

import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType
import org.drinkless.td.libcore.telegram.TdApi

interface EnterAuthCodeUseCase : BaseUseCase {

    @Suppress("DirectUseOfResultType")
    suspend fun checkAuthenticationCodeCatching(enteredAuthCode: CorrectAuthCodeType): Result<TdApi.Ok>

}
