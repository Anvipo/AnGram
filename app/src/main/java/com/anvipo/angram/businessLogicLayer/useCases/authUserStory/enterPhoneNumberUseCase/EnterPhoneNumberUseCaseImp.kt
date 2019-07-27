package com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType

class EnterPhoneNumberUseCaseImp(
    private val tdLibGateway: AuthorizationTDLibGateway
) : EnterPhoneNumberUseCase {

    override suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit> = tdLibGateway
        .setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
        .map {}

}