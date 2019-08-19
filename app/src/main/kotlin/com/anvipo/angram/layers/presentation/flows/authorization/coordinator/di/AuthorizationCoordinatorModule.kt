package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di

import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateSendChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.AuthorizationCoordinatorImpl
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.authorization.AuthorizationScreensFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.di.AddProxyModule.addProxyScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule.enterAuthenticationPhoneNumberScreenFactoryQualifier
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
    private val authorizationScreensFactoryQualifier: StringQualifier = named("authorizationScreensFactory")

    val authorizationCoordinatorScopeQualifier: StringQualifier = named("authorizationCoordinatorScope")

    lateinit var authorizationCoordinatorScope: Scope

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

        scope(authorizationCoordinatorScopeQualifier) {

            scoped<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
                AuthorizationCoordinatorImpl(
                    router = get(routerQualifier),
                    screensFactory = get(authorizationScreensFactoryQualifier),
                    tdApiUpdateAuthorizationStateReceiveChannel =
                    get(tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier),
                    systemMessageSendChannel = get(systemMessageSendChannelQualifier)
                )
            }

        }

        single<AuthorizationScreensFactory>(authorizationScreensFactoryQualifier) {
            AuthorizationScreensFactoryImpl(
                enterAuthenticationPhoneNumberScreenFactory = get(enterAuthenticationPhoneNumberScreenFactoryQualifier),
                enterAuthenticationPasswordScreenFactory = get(enterAuthenticationPasswordScreenFactoryQualifier),
                enterAuthenticationCodeScreenFactory = get(enterAuthenticationCodeScreenFactoryQualifier),
                addProxyScreenFactory = get(addProxyScreenFactoryQualifier)
            )
        }

    }

}