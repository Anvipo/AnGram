package com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway

class EnterAuthCodeUseCaseImp(
    private val tdLibGateway: TDLibGateway
) : EnterAuthCodeUseCase