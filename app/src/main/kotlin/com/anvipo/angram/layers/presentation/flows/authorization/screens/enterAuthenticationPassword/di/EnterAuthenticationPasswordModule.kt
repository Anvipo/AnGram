package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.tdApiUpdateConnectionStateAuthorizationFlowReceiveChannelQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.navigation.EnterPasswordScreen
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModelFactory
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModelImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object EnterAuthenticationPasswordModule {

    val module: Module = module {

        scope(authorizationCoordinatorScopeQualifier) {

            scoped<EnterAuthenticationPasswordScreenFactory> {
                EnterAuthenticationPasswordScreenFactoryImpl(
                    koinScope = this
                )
            }

            factory {
                EnterAuthenticationPasswordFragment.createNewInstance()
            }

            factory {
                EnterPasswordScreen()
            }

            scoped {
                EnterAuthenticationPasswordViewModelFactory
            }

            factory<EnterAuthenticationPasswordViewModel> {
                EnterAuthenticationPasswordViewModelImpl(
                    routeEventHandler = authorizationCoordinatorScope!!.get(
                        authorizationCoordinatorQualifier
                    ),
                    useCase = authorizationCoordinatorScope!!.get(),
                    resourceManager = get(),
                    tdApiUpdateConnectionStateReceiveChannel = authorizationCoordinatorScope!!.get(
                        tdApiUpdateConnectionStateAuthorizationFlowReceiveChannelQualifier
                    )
                )
            }

        }

    }

}