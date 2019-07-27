package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthenticationCodeUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.presenter.EnterAuthenticationCodePresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.presenter.EnterAuthenticationCodePresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthenticationCodeModule {

    internal val enterAuthenticationCodePresenterQualifier = named("enterAuthenticationCodePresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterAuthenticationCodePresenter>(enterAuthenticationCodePresenterQualifier) {
            EnterAuthenticationCodePresenterImp(
                routeEventHandler = get<AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<EnterAuthenticationCodeUseCase>(enterAuthenticationCodeUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}