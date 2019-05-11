package com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType
import org.drinkless.td.libcore.telegram.TdApi

class EnterPhoneNumberUseCaseImp(
    private val tdLibGateway: TDLibGateway
) : EnterPhoneNumberUseCase {

    @Suppress("DirectUseOfResultType")
    override suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<TdApi.Ok> = tdLibGateway.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)

}