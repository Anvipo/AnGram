package com.anvipo.angram.businessLogicLayer.di

import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCaseImp
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCaseImp
import org.koin.core.module.Module
import org.koin.dsl.module


object UseCasesModule {

    val module: Module = module {

        single<EnterPhoneNumberUseCase> {
            EnterPhoneNumberUseCaseImp(
                tdLibGateway = get()
            )
        }
        single<EnterAuthCodeUseCase> {
            EnterAuthCodeUseCaseImp(
                tdLibGateway = get()
            )
        }

    }

}