package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.coordinator.di.ApplicationRootCoordinatorModule
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

    private val systemMessageNotifier: StringQualifier = named("systemMessageNotifier")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinator> {
            ApplicationCoordinatorImp(
                coordinatorsFactory =
                get<ApplicationCoordinatorsFactory>(
                    ApplicationRootCoordinatorModule.applicationCoordinatorsFactory
                ),
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

    }

}
