package com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterPhoneNumber

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.types.CorrectPhoneNumberType

interface EnterPhoneNumberUseCase {

    suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit>

}