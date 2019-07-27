package com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType
import org.drinkless.td.libcore.telegram.TdApi

interface EnterPhoneNumberUseCase {

    suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit>

    suspend fun addProxyCatching(
        server: String,
        port: Int,
        enable: Boolean,
        type: TdApi.ProxyType
    ): Result<TdApi.Proxy>

}