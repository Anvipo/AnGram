package com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterPhoneNumber

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterPhoneNumber.types.CorrectPhoneNumberType

interface EnterPhoneNumberUseCase {

    suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit>

}