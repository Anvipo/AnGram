package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterPhoneNumberModule {

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterPhoneNumberPresenterImp>(enterPhoneNumberPresenter) {
            EnterPhoneNumberPresenterImp(
                useCase = get<EnterPhoneNumberUseCase>(enterPhoneNumberUseCase),
                coordinator = get<AuthorizationCoordinatorOutput>(
                    ApplicationRootCoordinatorModule.authorizationCoordinator
                ),
                resourceManager = get<ResourceManager>(SystemInfrastructureModule.resourceManager)
            )
        }

    }

    internal val enterPhoneNumberPresenter: StringQualifier = named("enterPhoneNumberPresenter")

}
