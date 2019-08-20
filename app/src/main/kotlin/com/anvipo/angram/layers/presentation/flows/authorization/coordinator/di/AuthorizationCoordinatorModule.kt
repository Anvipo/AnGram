package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di

import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateSendChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.AuthorizationCoordinatorImpl
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.authorization.AuthorizationScreensFactoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

object AuthorizationCoordinatorModule {

    val tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier: StringQualifier =
        named("tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannel")
    private val tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier =
        named("tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannel")
    private val tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier =
        named("tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannel")

    val authorizationCoordinatorQualifier: StringQualifier = named("authorizationCoordinator")

    val authorizationCoordinatorScopeQualifier: StringQualifier = named("authorizationCoordinatorScope")

    lateinit var authorizationCoordinatorScope: Scope

    @ExperimentalCoroutinesApi
    val module: Module = module {

        single<TdApiUpdateAuthorizationStateSendChannel>(
            tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
        ) {
            get(tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier)
        }
        factory(
            tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier
        ) {
            get<TdApiUpdateAuthorizationStateBroadcastChannel>(
                tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateAuthorizationStateBroadcastChannel>(
            tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier
        ) {
            BroadcastChannel(Channel.CONFLATED)
        }

        scope(authorizationCoordinatorScopeQualifier) {

            scoped<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
                AuthorizationCoordinatorImpl(
                    router = get(),
                    screensFactory = get(),
                    tdApiUpdateAuthorizationStateReceiveChannel =
                    get(tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier),
                    systemMessageSendChannel = get(systemMessageSendChannelQualifier)
                )
            }

        }

        single<AuthorizationScreensFactory> {
            AuthorizationScreensFactoryImpl(
                enterAuthenticationPhoneNumberScreenFactory = get(),
                enterAuthenticationPasswordScreenFactory = get(),
                enterAuthenticationCodeScreenFactory = get(),
                addProxyScreenFactory = get()
            )
        }

    }

}