package com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase

import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import org.drinkless.td.libcore.telegram.TdApi

interface EnterPhoneNumberUseCase : BaseUseCase {

    @Suppress("DirectUseOfResultType")
    suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok>

}