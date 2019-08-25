package com.anvipo.angram.layers.presentation.flows.main.coordinator.di

import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.presentation.flows.main.coordinator.MainCoordinatorImpl
import com.anvipo.angram.layers.presentation.flows.main.coordinator.interfaces.MainCoordinator
import com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.main.MainScreensFactory
import com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.main.MainScreensFactoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

object MainCoordinatorModule {

    val mainCoordinatorQualifier: StringQualifier = named("mainCoordinator")

    val mainCoordinatorScopeQualifier: StringQualifier = named("mainCoordinatorScope")

    lateinit var mainCoordinatorScope: Scope

    val module: Module = module {

        scope(mainCoordinatorScopeQualifier) {

            scoped<MainCoordinator>(mainCoordinatorQualifier) {
                MainCoordinatorImpl(
                    router = get(),
                    screensFactory = get(),
                    systemMessageSendChannel = get(systemMessageSendChannelQualifier)
                )
            }

            scoped<MainScreensFactory> {
                MainScreensFactoryImpl(
                    chatListScreenFactory = get()
                )
            }

        }

    }

}