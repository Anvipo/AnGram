package com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterPhoneNumber

import com.anvipo.angram.presentationLayer.flows.auth.screens.enterPhoneNumber.types.CorrectPhoneNumberType

interface EnterPhoneNumberUseCase {

    suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit>

}