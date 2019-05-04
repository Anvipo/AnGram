package com.anvipo.angram.applicationLayer.assemblies

import com.anvipo.angram.applicationLayer.launchSystem.AppActivity
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.businessLogicLayer.assemblies.GatewaysAssembly
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import org.drinkless.td.libcore.telegram.Client

object LaunchSystemAssembly {

    val applicationCoordinator: Coordinatorable by lazy {
        ApplicationCoordinator(
            coordinatorsFactory = ApplicationCoordinatorsFactoryImp,
            router = SystemInfrastructureAssembly.router,
            tdLibGateway = GatewaysAssembly.tdLibGateway
        )
    }

    val tgClient: Client by lazy {
        Client.create(
            AppActivity.updatesHandler,
            AppActivity.updatesExceptionHandler,
            AppActivity.defaultExceptionHandler
        )
    }

}