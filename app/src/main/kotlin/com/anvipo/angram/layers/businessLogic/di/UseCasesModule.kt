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
import com.anvipo.angram.layers.data.di.GatewaysModule.tdClientScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import org.koin.core.module.Module
import org.koin.dsl.module

object UseCasesModule {

    val module: Module = module {

        scope(tdClientScopeQualifier) {

            scoped<AppUseCase> {
                AppUseCaseImpl(
                    sharedPreferencesGateway = get()
                )
            }

        }


        scope(authorizationCoordinatorScopeQualifier) {

            scoped<EnterAuthenticationPhoneNumberUseCase> {
                EnterAuthenticationPhoneNumberUseCaseImpl(
                    authorizationTDLibGateway = authorizationCoordinatorScope!!.get()
                )
            }

            scoped<EnterAuthenticationCodeUseCase> {
                EnterAuthenticationCodeUseCaseImpl(
                    tdLibGateway = authorizationCoordinatorScope!!.get()
                )
            }

            scoped<EnterAuthenticationPasswordUseCase> {
                EnterAuthenticationPasswordUseCaseImpl(
                    tdLibGateway = authorizationCoordinatorScope!!.get()
                )
            }

        }


        factory<AddProxyUseCase> {
            AddProxyUseCaseImpl(
                tdLibGateway = get(),
                dbGateway = get()
            )
        }

    }

}