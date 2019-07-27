package com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationPasswordUseCase

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import org.drinkless.td.libcore.telegram.TdApi

class EnterAuthenticationPasswordUseCaseImp(
    private val tdLibGateway: AuthorizationTDLibGateway
) : EnterAuthenticationPasswordUseCase {

    override suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<TdApi.Ok> =
        tdLibGateway.checkAuthenticationPasswordCatching(
            enteredAuthenticationPassword = enteredAuthenticationPassword
        )

}