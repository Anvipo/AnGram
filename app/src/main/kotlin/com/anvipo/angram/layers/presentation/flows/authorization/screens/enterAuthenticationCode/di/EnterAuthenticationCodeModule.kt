package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.tdApiUpdateConnectionStateAuthorizationFlowReceiveChannelQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation.EnterAuthenticationCodeScreen
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation.EnterAuthenticationCodeScreenParameters
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModelFactory
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModelImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object EnterAuthenticationCodeModule {

    val module: Module = module {

        scope(authorizationCoordinatorScopeQualifier) {

            scoped<EnterAuthenticationCodeScreenFactory> {
                EnterAuthenticationCodeScreenFactoryImpl(
                    koinScope = this
                )
            }

            factory { (parameters: EnterAuthenticationCodeScreenParameters) ->
                EnterAuthenticationCodeFragment.createNewInstance(
                    shouldShowBackButton = parameters.shouldShowBackButton,
                    expectedCodeLength = parameters.expectedCodeLength,
                    enteredPhoneNumber = parameters.enteredPhoneNumber,
                    registrationRequired = parameters.registrationRequired,
                    termsOfServiceText = parameters.termsOfServiceText
                )
            }

            factory { (parameters: EnterAuthenticationCodeScreenParameters) ->
                EnterAuthenticationCodeScreen(parameters = parameters)
            }

            scoped {
                EnterAuthenticationCodeViewModelFactory
            }

            factory<EnterAuthenticationCodeViewModel> {
                EnterAuthenticationCodeViewModelImpl(
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