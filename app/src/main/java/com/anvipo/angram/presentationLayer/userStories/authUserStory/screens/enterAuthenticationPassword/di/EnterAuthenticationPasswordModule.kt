package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthenticationPasswordUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthenticationPasswordModule {

    internal val enterAuthenticationPasswordPresenterQualifier = named("enterAuthenticationPasswordPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterAuthenticationPasswordPresenter>(enterAuthenticationPasswordPresenterQualifier) {
            EnterAuthenticationPasswordPresenterImp(
                coordinator = get<AuthorizationCoordinatorEnterAuthenticationPasswordOutput>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}