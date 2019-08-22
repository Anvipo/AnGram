package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di

import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateSendChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.EnterAuthenticationPhoneNumberFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.navigation.EnterAuthenticationPhoneNumberScreen
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModelFactory
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModelImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthenticationPhoneNumberModule {

    private val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannelQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannel")
    val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier: StringQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannel")
    private val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannel")


    @ExperimentalCoroutinesApi
    val module: Module = module {

        scope(authorizationCoordinatorScopeQualifier) {

            scoped<EnterAuthenticationPhoneNumberScreenFactory> {
                EnterAuthenticationPhoneNumberScreenFactoryImpl
            }

            factory {
                EnterAuthenticationPhoneNumberScreen()
            }

            factory {
                EnterAuthenticationPhoneNumberFragment.createNewInstance()
            }


            scoped {
                EnterAuthenticationPhoneNumberViewModelFactory
            }

            factory<EnterAuthenticationPhoneNumberViewModel> {
                EnterAuthenticationPhoneNumberViewModelImpl(
                    routeEventHandler = authorizationCoordinatorScope!!.get(
                        authorizationCoordinatorQualifier
                    ),
                    useCase = authorizationCoordinatorScope!!.get(),
                    resourceManager = get(),
                    tdApiUpdateConnectionStateReceiveChannel = authorizationCoordinatorScope!!.get(
                        tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannelQualifier
                    )
                )
            }

            scoped<TdApiUpdateConnectionStateSendChannel>(
                tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier
            ) {
                authorizationCoordinatorScope!!.get(
                    tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier
                )
            }
            factory(
                tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannelQualifier
            ) {
                authorizationCoordinatorScope!!.get<TdApiUpdateConnectionStateBroadcastChannel>(
                    tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier
                ).openSubscription()
            }
            scoped<TdApiUpdateConnectionStateBroadcastChannel>(
                tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier
            ) {
                BroadcastChannel(Channel.CONFLATED)
            }

        }

    }

}
