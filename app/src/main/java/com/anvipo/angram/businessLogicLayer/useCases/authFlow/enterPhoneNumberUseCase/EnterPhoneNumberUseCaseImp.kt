package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterPhoneNumberUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.types.CorrectPhoneNumberType

class EnterPhoneNumberUseCaseImp(
    private val authorizationTDLibGateway: AuthorizationTDLibGateway
) : EnterPhoneNumberUseCase {

    override suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit> = authorizationTDLibGateway
        .setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
        .map {}

}