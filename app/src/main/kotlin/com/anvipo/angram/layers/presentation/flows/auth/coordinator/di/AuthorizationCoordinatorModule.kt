package com.anvipo.angram.layers.presentation.flows.auth.coordinator.di

import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateSendChannel
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.AuthorizationCoordinatorImpl
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactoryImpl
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule.addProxyScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberScreenFactoryQualifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AuthorizationCoordinatorModule {

    val tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier: StringQualifier =
        named("tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannel")
    private val tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier =
        named("tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannel")
    private val tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier =
        named("tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannel")

    val authorizationCoordinatorQualifier: StringQualifier = named("authorizationCoordinator")
    private val authorizationScreensFactoryQualifier: StringQualifier = named("authorizationScreensFactory")

    @ExperimentalCoroutinesApi
    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<TdApiUpdateAuthorizationStateSendChannel>(
            tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
        ) {
            get(tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier)
        }
        factory<TdApiUpdateAuthorizationStateReceiveChannel>(
            tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier
        ) {
            get<TdApiUpdateAuthorizationStateBroadcastChannel>(
                tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateAuthorizationStateBroadcastChannel>(
            tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier
        ) {
            BroadcastChannel<TdApiUpdateAuthorizationState>(Channel.CONFLATED)
        }

        factory<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
            AuthorizationCoordinatorImpl(
                router = get(routerQualifier),
                screensFactory = get(authorizationScreensFactoryQualifier),
                tdApiUpdateAuthorizationStateReceiveChannel =
                get(tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier),
                systemMessageSendChannel = get(systemMessageSendChannelQualifier)
            )
        }

        factory<AuthorizationScreensFactory>(authorizationScreensFactoryQualifier) {
            AuthorizationScreensFactoryImpl(
                enterPhoneNumberScreenFactory = get(enterPhoneNumberScreenFactoryQualifier),
                enterAuthenticationPasswordScreenFactory = get(enterAuthenticationPasswordScreenFactoryQualifier),
                enterAuthenticationCodeScreenFactory = get(enterAuthenticationCodeScreenFactoryQualifier),
                addProxyScreenFactory = get(addProxyScreenFactoryQualifier)
            )
        }

    }

}