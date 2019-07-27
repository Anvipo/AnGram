package com.anvipo.angram.businessLogicLayer.di

import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase.EnterPhoneNumberUseCaseImp
import com.anvipo.angram.dataLayer.di.GatewaysModule
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy.ProxyTDLibGateway
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


object UseCasesModule {

    internal val enterPhoneNumberUseCaseQualifier = named("enterPhoneNumberUseCase")
    internal val enterAuthenticationCodeUseCaseQualifier = named("enterAuthenticationCodeUseCase")
    internal val enterAuthenticationPasswordUseCaseQualifier = named("enterAuthenticationPasswordUseCase")
    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterPhoneNumberUseCase>(enterPhoneNumberUseCaseQualifier) {
            EnterPhoneNumberUseCaseImp(
                authorizationTDLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier),
                proxyTDLibGateway = get<ProxyTDLibGateway>(GatewaysModule.proxyTDLibGatewayQualifier)
            )
        }

        single<EnterAuthenticationCodeUseCase>(enterAuthenticationCodeUseCaseQualifier) {
            EnterAuthenticationCodeUseCaseImp(
                tdLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier)
            )
        }

        single<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCaseQualifier) {
            EnterAuthenticationPasswordUseCaseImp(
                tdLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier)
            )
        }
    }

}