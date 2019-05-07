package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinatorImp
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

typealias CorrectPhoneNumberType = String
typealias CorrectAuthCodeType = String
typealias BackButtonPressedType = Unit

object LaunchSystemModule {

    private val enteredCorrectPhoneNumberBroadcastChannel: StringQualifier =
        named("enteredCorrectPhoneNumberBroadcastChannel")
    internal val enteredCorrectPhoneNumberReceiveChannel: StringQualifier =
        named("enteredCorrectPhoneNumberReceiveChannel")
    internal val enteredCorrectPhoneNumberSendChannel: StringQualifier =
        named("enteredCorrectPhoneNumberSendChannel")

    private val backButtonPressedInPhoneNumberScreenBroadcastChannel: StringQualifier =
        named("backButtonPressedInPhoneNumberScreenBroadcastChannel")
    internal val backButtonPressedInPhoneNumberScreenReceiveChannel: StringQualifier =
        named("backButtonPressedInPhoneNumberScreenReceiveChannel")
    internal val backButtonPressedInPhoneNumberScreenSendChannel: StringQualifier =
        named("backButtonPressedInPhoneNumberScreenSendChannel")


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
    internal val backButtonPressedInAuthCodeScreenSendChannel: StringQualifier =
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



        single<BroadcastChannel<CorrectPhoneNumberType>>(enteredCorrectPhoneNumberBroadcastChannel) {
            BroadcastChannel(1)
        }
        single<ReceiveChannel<CorrectPhoneNumberType>>(enteredCorrectPhoneNumberReceiveChannel) {
            get<BroadcastChannel<CorrectPhoneNumberType>>(enteredCorrectPhoneNumberBroadcastChannel).openSubscription()
        }
        single<SendChannel<CorrectPhoneNumberType>>(enteredCorrectPhoneNumberSendChannel) {
            get<BroadcastChannel<CorrectPhoneNumberType>>(enteredCorrectPhoneNumberBroadcastChannel)
        }

        single<BroadcastChannel<BackButtonPressedType>>(backButtonPressedInPhoneNumberScreenBroadcastChannel) {
            BroadcastChannel(1)
        }
        single<ReceiveChannel<BackButtonPressedType>>(backButtonPressedInPhoneNumberScreenReceiveChannel) {
            get<BroadcastChannel<BackButtonPressedType>>(backButtonPressedInPhoneNumberScreenBroadcastChannel).openSubscription()
        }
        single<SendChannel<BackButtonPressedType>>(backButtonPressedInPhoneNumberScreenSendChannel) {
            get<BroadcastChannel<BackButtonPressedType>>(backButtonPressedInPhoneNumberScreenBroadcastChannel)
        }


        single<BroadcastChannel<CorrectAuthCodeType>>(enteredCorrectAuthCodeBroadcastChannel) {
            BroadcastChannel(1)
        }
        single<ReceiveChannel<CorrectAuthCodeType>>(enteredCorrectAuthCodeReceiveChannel) {
            get<BroadcastChannel<CorrectAuthCodeType>>(enteredCorrectAuthCodeBroadcastChannel).openSubscription()
        }
        single<SendChannel<CorrectAuthCodeType>>(enteredCorrectAuthCodeSendChannel) {
            get<BroadcastChannel<CorrectAuthCodeType>>(enteredCorrectAuthCodeBroadcastChannel)
        }

        single<BroadcastChannel<BackButtonPressedType>>(backButtonPressedInAuthCodeScreenBroadcastChannel) {
            BroadcastChannel(1)
        }
        single<ReceiveChannel<BackButtonPressedType>>(backButtonPressedInAuthCodeScreenReceiveChannel) {
            get<BroadcastChannel<BackButtonPressedType>>(backButtonPressedInAuthCodeScreenBroadcastChannel).openSubscription()
        }
        single<SendChannel<BackButtonPressedType>>(backButtonPressedInAuthCodeScreenSendChannel) {
            get<BroadcastChannel<BackButtonPressedType>>(backButtonPressedInAuthCodeScreenBroadcastChannel)
        }

    }

}
