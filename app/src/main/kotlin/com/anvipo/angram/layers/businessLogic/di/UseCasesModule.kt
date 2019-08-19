package com.anvipo.angram.layers.businessLogic.di

import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCase
import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.addProxy.AddProxyUseCase
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.addProxy.AddProxyUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationCode.EnterAuthenticationCodeUseCase
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationCode.EnterAuthenticationCodeUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPassword.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPassword.EnterAuthenticationPasswordUseCaseImpl
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberUseCase
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberUseCaseImpl
import com.anvipo.angram.layers.data.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.proxyLocalGatewayQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.proxyTDLibGatewayQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.sharedPreferencesGatewayQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.tdClientScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import org.koin.core.module.Module
import org.koin.dsl.module

object UseCasesModule {

    val module: Module = module {

        scope(tdClientScopeQualifier) {

            factory<AppUseCase> {
                AppUseCaseImpl(
                    sharedPreferencesGateway = get(sharedPreferencesGatewayQualifier)
                )
            }

        }


        scope(authorizationCoordinatorScopeQualifier) {

            factory<EnterAuthenticationPhoneNumberUseCase> {
                EnterAuthenticationPhoneNumberUseCaseImpl(
                    authorizationTDLibGateway = authorizationCoordinatorScope.get(authorizationTDLibGatewayQualifier)
                )
            }

            factory<EnterAuthenticationCodeUseCase> {
                EnterAuthenticationCodeUseCaseImpl(
                    tdLibGateway = authorizationCoordinatorScope.get(authorizationTDLibGatewayQualifier)
                )
            }

            factory<EnterAuthenticationPasswordUseCase> {
                EnterAuthenticationPasswordUseCaseImpl(
                    tdLibGateway = authorizationCoordinatorScope.get(authorizationTDLibGatewayQualifier)
                )
            }

        }


        factory<AddProxyUseCase> {
            AddProxyUseCaseImpl(
                tdLibGateway = get(proxyTDLibGatewayQualifier),
                dbGateway = get(proxyLocalGatewayQualifier)
            )
        }

    }

}