package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di

import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateSendChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateSendChannel
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

    val authorizationCoordinatorScopeQualifier: StringQualifier =
        named("authorizationCoordinatorScope")

    var authorizationCoordinatorScope: Scope? = null

    val tdApiUpdateConnectionStateAuthorizationFlowSendChannelQualifier: StringQualifier =
        named("tdApiUpdateConnectionStateAuthorizationFlowSendChannel")
    val tdApiUpdateConnectionStateAuthorizationFlowReceiveChannelQualifier: StringQualifier =
        named("tdApiUpdateConnectionStateAuthorizationFlowReceiveChannel")
    private val tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannelQualifier =
        named("tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannel")

    @ExperimentalCoroutinesApi
    val module: Module = module {

        scope(authorizationCoordinatorScopeQualifier) {

            scoped<TdApiUpdateConnectionStateSendChannel>(
                tdApiUpdateConnectionStateAuthorizationFlowSendChannelQualifier
            ) {
                authorizationCoordinatorScope!!.get(
                    tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannelQualifier
                )
            }
            factory(
                tdApiUpdateConnectionStateAuthorizationFlowReceiveChannelQualifier
            ) {
                authorizationCoordinatorScope!!.get<TdApiUpdateConnectionStateBroadcastChannel>(
                    tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannelQualifier
                ).openSubscription()
            }
            scoped<TdApiUpdateConnectionStateBroadcastChannel>(
                tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannelQualifier
            ) {
                BroadcastChannel(Channel.CONFLATED)
            }

            scoped<TdApiUpdateAuthorizationStateSendChannel>(
                tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
            ) {
                authorizationCoordinatorScope!!.get(
                    tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier
                )
            }
            factory(
                tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier
            ) {
                authorizationCoordinatorScope!!.get<TdApiUpdateAuthorizationStateBroadcastChannel>(
                    tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier
                ).openSubscription()
            }
            scoped<TdApiUpdateAuthorizationStateBroadcastChannel>(
                tdApiUpdateAuthorizationStateAuthorizationCoordinatorBroadcastChannelQualifier
            ) {
                BroadcastChannel(Channel.CONFLATED)
            }

            scoped<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
                AuthorizationCoordinatorImpl(
                    router = get(),
                    screensFactory = authorizationCoordinatorScope!!.get(),
                    tdApiUpdateAuthorizationStateReceiveChannel =
                    authorizationCoordinatorScope!!.get(
                        tdApiUpdateAuthorizationStateAuthorizationCoordinatorReceiveChannelQualifier
                    ),
                    systemMessageSendChannel = authorizationCoordinatorScope!!.get(
                        systemMessageSendChannelQualifier
                    )
                )
            }

            scoped<AuthorizationScreensFactory> {
                AuthorizationScreensFactoryImpl(
                    enterAuthenticationPhoneNumberScreenFactory =
                    authorizationCoordinatorScope!!.get(),
                    enterAuthenticationPasswordScreenFactory =
                    authorizationCoordinatorScope!!.get(),
                    enterAuthenticationCodeScreenFactory = authorizationCoordinatorScope!!.get(),
                    addProxyScreenFactory = authorizationCoordinatorScope!!.get()
                )
            }

        }

    }

}