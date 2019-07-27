package com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy.ProxyTDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType
import org.drinkless.td.libcore.telegram.TdApi

class EnterPhoneNumberUseCaseImp(
    private val authorizationTDLibGateway: AuthorizationTDLibGateway,
    private val proxyTDLibGateway: ProxyTDLibGateway
) : EnterPhoneNumberUseCase {

    override suspend fun addProxyCatching(
        server: String,
        port: Int,
        enable: Boolean,
        type: TdApi.ProxyType
    ): Result<TdApi.Proxy> =
        proxyTDLibGateway
            .addProxyCatching(server, port, enable, type)

    override suspend fun setAuthenticationPhoneNumberCatching(
        enteredPhoneNumber: CorrectPhoneNumberType
    ): Result<Unit> = authorizationTDLibGateway
        .setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
        .map {}

}