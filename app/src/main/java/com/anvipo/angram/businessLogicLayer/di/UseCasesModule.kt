package com.anvipo.angram.businessLogicLayer.di

import com.anvipo.angram.businessLogicLayer.useCases.app.AppUseCase
import com.anvipo.angram.businessLogicLayer.useCases.app.AppUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.addProxyUseCase.AddProxyUseCase
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.addProxyUseCase.AddProxyUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterPhoneNumberUseCase.EnterPhoneNumberUseCaseImp
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.proxyLocalGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.proxyTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.sharedPreferencesGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.local.db.room.proxy.ProxyRoomDAO
import com.anvipo.angram.dataLayer.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.proxy.ProxyTDLibGateway
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


object UseCasesModule {

    internal val appUseCaseQualifier = named("appUseCase")

    internal val enterPhoneNumberUseCaseQualifier = named("enterPhoneNumberUseCase")
    internal val enterAuthenticationCodeUseCaseQualifier = named("enterAuthenticationCodeUseCase")
    internal val enterAuthenticationPasswordUseCaseQualifier = named("enterAuthenticationPasswordUseCase")

    internal val addProxyUseCaseQualifier = named("addProxyUseCase")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<AppUseCase>(appUseCaseQualifier) {
            AppUseCaseImp(
                proxyTDLibGateway = get<ProxyTDLibGateway>(proxyTDLibGatewayQualifier),
                sharedPreferencesGateway = get<SharedPreferencesDAO>(sharedPreferencesGatewayQualifier)
            )
        }

        single<EnterPhoneNumberUseCase>(enterPhoneNumberUseCaseQualifier) {
            EnterPhoneNumberUseCaseImp(
                authorizationTDLibGateway =
                get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier)
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

        single<AddProxyUseCase>(addProxyUseCaseQualifier) {
            AddProxyUseCaseImp(
                tdLibGateway = get<ProxyTDLibGateway>(proxyTDLibGatewayQualifier),
                dbGateway = get<ProxyRoomDAO>(proxyLocalGatewayQualifier)
            )
        }

    }

}