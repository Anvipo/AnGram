package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterPhoneNumberUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterPhoneNumberModule {

    internal val enterPhoneNumberPresenterQualifier = named("enterPhoneNumberPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterPhoneNumberPresenter>(enterPhoneNumberPresenterQualifier) {
            EnterPhoneNumberPresenterImp(
                routeEventHandler = get<AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<EnterPhoneNumberUseCase>(enterPhoneNumberUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}
