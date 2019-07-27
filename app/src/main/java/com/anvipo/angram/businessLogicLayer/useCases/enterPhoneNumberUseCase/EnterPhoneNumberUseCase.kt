package com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase

import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType
import org.drinkless.td.libcore.telegram.TdApi

interface EnterPhoneNumberUseCase : BaseUseCase {

    suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: CorrectPhoneNumberType): Result<TdApi.Ok>

}