package com.anvipo.angram.layers.businessLogic.di

import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCase
import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCaseImp
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.addProxy.AddProxyUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.addProxy.AddProxyUseCaseImp
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationCode.EnterAuthenticationCodeUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationCode.EnterAuthenticationCodeUseCaseImp
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword.EnterAuthenticationPasswordUseCaseImp
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterPhoneNumber.EnterPhoneNumberUseCase
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterPhoneNumber.EnterPhoneNumberUseCaseImp
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.proxyLocalGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.proxyTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.sharedPreferencesGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.local.db.room.proxy.ProxyRoomDAO
import com.anvipo.angram.dataLayer.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.proxy.ProxyTDLibGateway
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

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<AppUseCase>(appUseCaseQualifier) {
            AppUseCaseImp(
                proxyTDLibGateway = get<ProxyTDLibGateway>(proxyTDLibGatewayQualifier),
                sharedPreferencesGateway = get<SharedPreferencesDAO>(sharedPreferencesGatewayQualifier)
            )
        }

        factory<EnterPhoneNumberUseCase>(enterPhoneNumberUseCaseQualifier) {
            EnterPhoneNumberUseCaseImp(
                authorizationTDLibGateway =
                get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier)
            )
        }

        factory<EnterAuthenticationCodeUseCase>(enterAuthenticationCodeUseCaseQualifier) {
            EnterAuthenticationCodeUseCaseImp(
                tdLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier)
            )
        }

        factory<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCaseQualifier) {
            EnterAuthenticationPasswordUseCaseImp(
                tdLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier)
            )
        }

        factory<AddProxyUseCase>(addProxyUseCaseQualifier) {
            AddProxyUseCaseImp(
                tdLibGateway = get<ProxyTDLibGateway>(proxyTDLibGatewayQualifier),
                dbGateway = get<ProxyRoomDAO>(proxyLocalGatewayQualifier)
            )
        }

    }

}