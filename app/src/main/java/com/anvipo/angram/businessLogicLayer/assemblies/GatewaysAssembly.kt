package com.anvipo.angram.businessLogicLayer.assemblies

import com.anvipo.angram.applicationLayer.launchSystem.AppActivity
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGatewayImp

object GatewaysAssembly {

    val tdLibGateway: TDLibGateway by lazy {
        TDLibGatewayImp(AppActivity.tgClient)
    }

}