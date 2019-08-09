package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.addProxyUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.addProxyUseCase.AddProxyUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.presenter.AddProxyPresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.presenter.AddProxyPresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AddProxyModule {

    internal val addProxyPresenterQualifier = named("addProxyPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<AddProxyPresenter>(addProxyPresenterQualifier) {
            AddProxyPresenterImp(
                routeEventHandler = get<AuthorizationCoordinatorAddProxyRouteEventHandler>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<AddProxyUseCase>(addProxyUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}