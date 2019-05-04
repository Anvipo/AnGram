package com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase

import org.drinkless.td.libcore.telegram.TdApi

interface EnterPhoneNumberUseCase {

    @Suppress("DirectUseOfResultType")
    suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok>

}