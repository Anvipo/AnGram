package com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType
import org.drinkless.td.libcore.telegram.TdApi

class EnterAuthCodeUseCaseImp(
    private val tdLibGateway: TDLibGateway
) : EnterAuthCodeUseCase {

    @Suppress("DirectUseOfResultType")
    override suspend fun checkAuthenticationCodeCatching(
        enteredAuthCode: CorrectAuthCodeType
    ): Result<TdApi.Ok> = tdLibGateway.checkAuthenticationCodeCatching(enteredAuthCode)

}