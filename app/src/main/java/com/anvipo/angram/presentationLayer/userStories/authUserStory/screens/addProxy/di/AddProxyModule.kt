package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.presenter.AddProxyPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.presenter.AddProxyPresenterImp
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
                )
            )
        }

    }

}