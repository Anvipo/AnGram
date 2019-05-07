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
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


object LaunchSystemModule {

    internal val enterCorrectPhoneNumberNotifier: StringQualifier = named("enterCorrectPhoneNumberNotifier")

    internal val backButtonPressedInPhoneNumberScreen: StringQualifier = named("backButtonPressedInPhoneNumberScreen")

    internal val backButtonPressedInAuthCodeScreen: StringQualifier = named("backButtonPressedInAuthCodeScreen")

    internal val enterCorrectAuthCodeNotifier: StringQualifier = named("enterCorrectAuthCodeNotifier")

    private val systemMessageNotifier: StringQualifier = named("systemMessageNotifier")


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

        single<IReceiveDataNotifier<String>>(enterCorrectPhoneNumberNotifier) {
            DataNotifier()
        }

        single<IReceiveDataNotifier<String>>(enterCorrectAuthCodeNotifier) {
            DataNotifier()
        }

        single<IReceiveDataNotifier<Unit>>(backButtonPressedInPhoneNumberScreen) {
            DataNotifier()
        }

        single<IReceiveDataNotifier<Unit>>(backButtonPressedInAuthCodeScreen) {
            DataNotifier()
        }

    }

}
