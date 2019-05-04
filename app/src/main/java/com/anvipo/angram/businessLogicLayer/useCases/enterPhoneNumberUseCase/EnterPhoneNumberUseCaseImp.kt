package com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import org.drinkless.td.libcore.telegram.TdApi

class EnterPhoneNumberUseCaseImp(
    private val tdLibGateway: TDLibGateway
) : EnterPhoneNumberUseCase {

    @Suppress("DirectUseOfResultType")
    override suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok> {
        return tdLibGateway.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
    }

}