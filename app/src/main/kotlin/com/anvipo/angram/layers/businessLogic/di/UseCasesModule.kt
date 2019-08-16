package com.anvipo.angram.layers.businessLogic.di

import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCase
import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.addProxy.AddProxyUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.addProxy.AddProxyUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationCode.EnterAuthenticationCodeUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationCode.EnterAuthenticationCodeUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword.EnterAuthenticationPasswordUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterPhoneNumber.EnterPhoneNumberUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterPhoneNumber.EnterPhoneNumberUseCaseImpl
import com.anvipo.angram.layers.data.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.proxyLocalGatewayQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.proxyTDLibGatewayQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.sharedPreferencesGatewayQualifier
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


object UseCasesModule {

    val appUseCaseQualifier: StringQualifier = named("appUseCase")

    val enterPhoneNumberUseCaseQualifier: StringQualifier = named("enterPhoneNumberUseCase")
    val enterAuthenticationCodeUseCaseQualifier: StringQualifier = named("enterAuthenticationCodeUseCase")
    val enterAuthenticationPasswordUseCaseQualifier: StringQualifier = named("enterAuthenticationPasswordUseCase")

    val addProxyUseCaseQualifier: StringQualifier = named("addProxyUseCase")

    val module: Module = module {

        factory<AppUseCase>(appUseCaseQualifier) {
            AppUseCaseImpl(
                sharedPreferencesGateway = get(sharedPreferencesGatewayQualifier)
            )
        }

        factory<EnterPhoneNumberUseCase>(enterPhoneNumberUseCaseQualifier) {
            EnterPhoneNumberUseCaseImpl(
                authorizationTDLibGateway = get(authorizationTDLibGatewayQualifier)
            )
        }

        factory<EnterAuthenticationCodeUseCase>(enterAuthenticationCodeUseCaseQualifier) {
            EnterAuthenticationCodeUseCaseImpl(
                tdLibGateway = get(authorizationTDLibGatewayQualifier)
            )
        }

        factory<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCaseQualifier) {
            EnterAuthenticationPasswordUseCaseImpl(
                tdLibGateway = get(authorizationTDLibGatewayQualifier)
            )
        }

        factory<AddProxyUseCase>(addProxyUseCaseQualifier) {
            AddProxyUseCaseImpl(
                tdLibGateway = get(proxyTDLibGatewayQualifier),
                dbGateway = get(proxyLocalGatewayQualifier)
            )
        }

    }

}