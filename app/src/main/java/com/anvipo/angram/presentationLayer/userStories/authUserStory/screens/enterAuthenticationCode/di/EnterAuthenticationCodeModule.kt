package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule.authorizationCoordinator
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManager
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthenticationCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthCodeOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.presenter.EnterAuthenticationCodePresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthenticationCodeModule {

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterAuthenticationCodePresenterImp>(enterAuthenticationCodePresenter) {
            EnterAuthenticationCodePresenterImp(
                useCase = get<EnterAuthenticationCodeUseCase>(enterAuthenticationCodeUseCase),
                coordinator = get<AuthorizationCoordinatorEnterAuthCodeOutput>(authorizationCoordinator),
                resourceManager = get<ResourceManager>(resourceManager)
            )
        }

    }

    internal val enterAuthenticationCodePresenter: StringQualifier = named("enterAuthenticationCodePresenter")

}