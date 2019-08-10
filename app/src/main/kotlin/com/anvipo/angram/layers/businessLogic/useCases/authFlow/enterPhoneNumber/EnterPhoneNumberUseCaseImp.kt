package com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterPhoneNumber

import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.auth.screens.enterPhoneNumber.types.CorrectPhoneNumberType

class EnterPhoneNumberUseCaseImp(
    private val authorizationTDLibGateway: AuthorizationTDLibGateway
) : EnterPhoneNumberUseCase {

    override suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit> = authorizationTDLibGateway
        .setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
        .map {}

}