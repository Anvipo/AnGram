package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.di

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy.AddProxyScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.AddProxyFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.navigation.AddProxyScreen
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.navigation.AddProxyScreenParameters
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModelFactory
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModelImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object AddProxyModule {

    val module: Module = module {

        single<AddProxyScreenFactory> {
            AddProxyScreenFactoryImpl(
                koinScope = this
            )
        }

        factory { (parameters: AddProxyScreenParameters) ->
            AddProxyFragment.createNewInstance(
                shouldShowBackButton = parameters.shouldShowBackButton,
                proxyType = parameters.proxyType
            )
        }

        factory { (parameters: AddProxyScreenParameters) ->
            AddProxyScreen(parameters = parameters)
        }

        single {
            AddProxyViewModelFactory
        }


        factory<AddProxyViewModel> {
            AddProxyViewModelImpl(
                routeEventHandler = authorizationCoordinatorScope!!.get(authorizationCoordinatorQualifier),
                useCase = authorizationCoordinatorScope!!.get(),
                resourceManager = get()
            )
        }

    }

}