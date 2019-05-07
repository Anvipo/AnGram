package com.anvipo.angram.businessLogicLayer.di

import com.anvipo.angram.businessLogicLayer.di.GatewaysModule.tdLibGateway
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCaseImp
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


object UseCasesModule {

    internal val enterPhoneNumberUseCase: StringQualifier = named("enterPhoneNumberUseCase")
    internal val enterAuthCodeUseCase: StringQualifier = named("enterAuthCodeUseCase")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterPhoneNumberUseCase>(enterPhoneNumberUseCase) {
            EnterPhoneNumberUseCaseImp(
                tdLibGateway = get<TDLibGateway>(tdLibGateway)
            )
        }
        single<EnterAuthCodeUseCase>(enterAuthCodeUseCase) {
            EnterAuthCodeUseCaseImp(
                tdLibGateway = get<TDLibGateway>(tdLibGateway)
            )
        }

    }

}