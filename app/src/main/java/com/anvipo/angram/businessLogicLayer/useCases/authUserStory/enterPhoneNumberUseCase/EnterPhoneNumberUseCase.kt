package com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType

interface EnterPhoneNumberUseCase {

    suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit>

}