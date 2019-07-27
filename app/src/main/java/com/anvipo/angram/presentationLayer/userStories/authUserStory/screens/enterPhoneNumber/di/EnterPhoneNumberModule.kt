package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.applicationLayer.types.ConnectionState
import com.anvipo.angram.applicationLayer.types.ConnectionStateBroadcastChannel
import com.anvipo.angram.applicationLayer.types.ConnectionStateReceiveChannel
import com.anvipo.angram.applicationLayer.types.ConnectionStateSendChannel
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterPhoneNumberUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("EXPERIMENTAL_API_USAGE")
object EnterPhoneNumberModule {

    internal val enterPhoneNumberPresenterQualifier = named("enterPhoneNumberPresenter")

    private val connectionStateEnterPhoneNumberReceiveChannelQualifier =
        named("connectionStateEnterPhoneNumberReceiveChannel")
    internal val connectionStateEnterPhoneNumberSendChannelQualifier =
        named("connectionStateEnterPhoneNumberSendChannel")
    private val connectionStateEnterPhoneNumberBroadcastChannelQualifier =
        named("connectionStateEnterPhoneNumberBroadcastChannel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ConnectionStateSendChannel>(connectionStateEnterPhoneNumberSendChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateEnterPhoneNumberBroadcastChannelQualifier)
        }
        single<ConnectionStateReceiveChannel>(connectionStateEnterPhoneNumberReceiveChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateEnterPhoneNumberBroadcastChannelQualifier)
                .openSubscription()
        }
        single<ConnectionStateBroadcastChannel>(connectionStateEnterPhoneNumberBroadcastChannelQualifier) {
            BroadcastChannel<ConnectionState>(Channel.CONFLATED)
        }

        single<EnterPhoneNumberPresenter>(enterPhoneNumberPresenterQualifier) {
            EnterPhoneNumberPresenterImp(
                routeEventHandler = get<AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<EnterPhoneNumberUseCase>(enterPhoneNumberUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier),
                connectionStateReceiveChannel = get<ConnectionStateReceiveChannel>(
                    connectionStateEnterPhoneNumberReceiveChannelQualifier
                )
            )
        }

    }

}
