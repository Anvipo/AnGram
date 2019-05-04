package com.anvipo.angram.businessLogicLayer.assemblies

import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCaseImp

object UseCasesAssembly {

    val enterPhoneNumberUseCase: EnterPhoneNumberUseCase by lazy {
        val tdLibGateway = GatewaysAssembly.tdLibGateway

        EnterPhoneNumberUseCaseImp(tdLibGateway)
    }

}