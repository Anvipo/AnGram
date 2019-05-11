package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule.authorizationCoordinator
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManager
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthenticationPasswordUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthenticationPasswordModule {

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterAuthenticationPasswordPresenterImp>(enterAuthenticationPasswordPresenter) {
            EnterAuthenticationPasswordPresenterImp(
                useCase = get<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCase),
                coordinator = get<AuthorizationCoordinatorEnterAuthenticationPasswordOutput>(authorizationCoordinator),
                resourceManager = get<ResourceManager>(resourceManager)
            )
        }

    }

    internal val enterAuthenticationPasswordPresenter: StringQualifier =
        named("enterAuthenticationPasswordPresenter")

}