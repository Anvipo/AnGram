package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule.authorizationCoordinator
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManager
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthCodeOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter.EnterAuthCodePresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthCodeModule {

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterAuthCodePresenterImp>(enterAuthCodePresenter) {
            EnterAuthCodePresenterImp(
                useCase = get<EnterAuthCodeUseCase>(enterAuthCodeUseCase),
                coordinator = get<AuthorizationCoordinatorEnterAuthCodeOutput>(authorizationCoordinator),
                resourceManager = get<ResourceManager>(resourceManager)
            )
        }

    }

    internal val enterAuthCodePresenter: StringQualifier = named("enterAuthCodePresenter")

}