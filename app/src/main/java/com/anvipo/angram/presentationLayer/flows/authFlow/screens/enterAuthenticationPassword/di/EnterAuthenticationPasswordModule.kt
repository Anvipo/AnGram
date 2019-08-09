package com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthenticationPasswordUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthenticationPasswordModule {

    internal val enterAuthenticationPasswordPresenterQualifier = named("enterAuthenticationPasswordPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterAuthenticationPasswordPresenter>(enterAuthenticationPasswordPresenterQualifier) {
            EnterAuthenticationPasswordPresenterImp(
                routeEventHandler = get<AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}