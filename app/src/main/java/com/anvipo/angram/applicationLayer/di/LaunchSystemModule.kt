package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.types.BackButtonPressedBroadcastChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedSendChannel
import com.anvipo.angram.coreLayer.collections.IMutableStack
import com.anvipo.angram.coreLayer.collections.IReadOnlyStack
import com.anvipo.angram.coreLayer.collections.MutableStack
import com.anvipo.angram.coreLayer.message.DataNotifier
import com.anvipo.angram.coreLayer.message.IReceiveDataNotifier
import com.anvipo.angram.coreLayer.message.ISentDataNotifier
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


typealias CorrectAuthCodeType = String

typealias CorrectAuthCodeBroadcastChannel = BroadcastChannel<CorrectAuthCodeType>
typealias CorrectAuthCodeReceiveChannel = ReceiveChannel<CorrectAuthCodeType>
typealias CorrectAuthCodeSendChannel = SendChannel<CorrectAuthCodeType>

object LaunchSystemModule {

    private val enteredCorrectAuthCodeBroadcastChannel: StringQualifier =
        named("enteredCorrectAuthCodeBroadcastChannel")
    internal val enteredCorrectAuthCodeReceiveChannel: StringQualifier =
        named("enteredCorrectAuthCodeReceiveChannel")
    internal val enteredCorrectAuthCodeSendChannel: StringQualifier =
        named("enteredCorrectAuthCodeSendChannel")

    private val backButtonPressedInAuthCodeScreenBroadcastChannel: StringQualifier =
        named("backButtonPressedInAuthCodeScreenBroadcastChannel")
    internal val backButtonPressedInAuthCodeScreenReceiveChannel: StringQualifier =
        named("backButtonPressedInAuthCodeScreenReceiveChannel")
    private val backButtonPressedInAuthCodeScreenSendChannel: StringQualifier =
        named("backButtonPressedInAuthCodeScreenSendChannel")


    private val systemMessageNotifier: StringQualifier = named("systemMessageNotifier")


    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinator> {
            ApplicationCoordinatorImp(
                coordinatorsFactory = get(),
                router = get(),
                tdLibGateway = get(),
                systemMessageNotifier = get()
            )
        }

        single<IReadOnlyStack<TdApi.UpdateAuthorizationState>> {
            get<IMutableStack<TdApi.UpdateAuthorizationState>>()
        }

        single<IMutableStack<TdApi.UpdateAuthorizationState>> {
            MutableStack()
        }

        single<ISentDataNotifier<SystemMessage>> {
            get(systemMessageNotifier)
        }
        single<IReceiveDataNotifier<SystemMessage>> {
            get(systemMessageNotifier)
        }
        single<DataNotifier<SystemMessage>>(systemMessageNotifier) {
            DataNotifier()
        }


        single<CorrectAuthCodeBroadcastChannel>(enteredCorrectAuthCodeBroadcastChannel) {
            BroadcastChannel(1)
        }
        single<CorrectAuthCodeReceiveChannel>(enteredCorrectAuthCodeReceiveChannel) {
            get<CorrectAuthCodeBroadcastChannel>(
                enteredCorrectAuthCodeBroadcastChannel
            ).openSubscription()
        }
        single<CorrectAuthCodeSendChannel>(enteredCorrectAuthCodeSendChannel) {
            get<CorrectAuthCodeBroadcastChannel>(enteredCorrectAuthCodeBroadcastChannel)
        }

        single<BackButtonPressedBroadcastChannel>(backButtonPressedInAuthCodeScreenBroadcastChannel) {
            BroadcastChannel(1)
        }
        single<BackButtonPressedReceiveChannel>(backButtonPressedInAuthCodeScreenReceiveChannel) {
            get<BackButtonPressedBroadcastChannel>(backButtonPressedInAuthCodeScreenBroadcastChannel).openSubscription()
        }
        single<BackButtonPressedSendChannel>(backButtonPressedInAuthCodeScreenSendChannel) {
            get<BackButtonPressedBroadcastChannel>(backButtonPressedInAuthCodeScreenBroadcastChannel)
        }

    }

}
