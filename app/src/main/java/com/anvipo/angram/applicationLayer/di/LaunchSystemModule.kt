package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.coreLayer.collections.IMutableStack
import com.anvipo.angram.coreLayer.collections.IReadOnlyStack
import com.anvipo.angram.coreLayer.collections.MutableStack
import com.anvipo.angram.coreLayer.message.SystemMessageNotifier
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.dsl.module


object LaunchSystemModule {

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

        single {
            SystemMessageNotifier()
        }

    }

}
