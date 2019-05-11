package com.anvipo.angram.businessLogicLayer.di

import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCaseImp
import com.anvipo.angram.dataLayer.di.GatewaysModule.tdLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGateway
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


object UseCasesModule {

    internal val enterPhoneNumberUseCase: StringQualifier = named("enterPhoneNumberUseCase")
    internal val enterAuthenticationCodeUseCase: StringQualifier = named("enterAuthenticationCodeUseCase")
    internal val enterAuthenticationPasswordUseCase: StringQualifier = named("enterAuthenticationPasswordUseCase")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterPhoneNumberUseCase>(enterPhoneNumberUseCase) {
            EnterPhoneNumberUseCaseImp(
                tdLibGateway = get<TDLibGateway>(tdLibGateway)
            )
        }

        single<EnterAuthenticationCodeUseCase>(enterAuthenticationCodeUseCase) {
            EnterAuthenticationCodeUseCaseImp(
                tdLibGateway = get<TDLibGateway>(tdLibGateway)
            )
        }

        single<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCase) {
            EnterAuthenticationPasswordUseCaseImp(
                tdLibGateway = get<TDLibGateway>(tdLibGateway)
            )
        }

    }

}