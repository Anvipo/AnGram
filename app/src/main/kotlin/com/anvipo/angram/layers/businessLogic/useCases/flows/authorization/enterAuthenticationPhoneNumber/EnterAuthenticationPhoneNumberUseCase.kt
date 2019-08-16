package com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPhoneNumber

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.types.CorrectPhoneNumberType

interface EnterAuthenticationPhoneNumberUseCase {

    suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit>

}