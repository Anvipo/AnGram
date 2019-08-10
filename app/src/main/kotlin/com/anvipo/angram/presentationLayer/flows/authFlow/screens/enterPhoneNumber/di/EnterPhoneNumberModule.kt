package com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.applicationLayer.types.ConnectionStateBroadcastChannel
import com.anvipo.angram.applicationLayer.types.ConnectionStateReceiveChannel
import com.anvipo.angram.applicationLayer.types.ConnectionStateSendChannel
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterPhoneNumberUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterPhoneNumber.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactoryImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.view.EnterPhoneNumberView
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

@Suppress("EXPERIMENTAL_API_USAGE")
object EnterPhoneNumberModule {

    class EnterPhoneNumberScreen : SupportAppScreen() {
        override fun getFragment(): Fragment =
            GlobalContext.get().koin.get(enterPhoneNumberViewQualifier)
    }

    val enterPhoneNumberScreenFactoryQualifier: StringQualifier =
        named("enterPhoneNumberScreenFactory")
    val enterPhoneNumberViewQualifier: StringQualifier =
        named("enterPhoneNumberView")

    val enterPhoneNumberScreenQualifier: StringQualifier =
        named("enterPhoneNumberScreen")

    val enterPhoneNumberPresenterQualifier: StringQualifier =
        named("enterPhoneNumberPresenter")
    private val connectionStateEnterPhoneNumberReceiveChannelQualifier =
        named("connectionStateEnterPhoneNumberReceiveChannel")
    val connectionStateEnterPhoneNumberSendChannelQualifier: StringQualifier =
        named("connectionStateEnterPhoneNumberSendChannel")

    private val connectionStateEnterPhoneNumberBroadcastChannelQualifier =
        named("connectionStateEnterPhoneNumberBroadcastChannel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnterPhoneNumberScreenFactory>(enterPhoneNumberScreenFactoryQualifier) {
            EnterPhoneNumberScreenFactoryImp(
                koinScope = this
            )
        }

        factory<EnterPhoneNumberView>(
            enterPhoneNumberViewQualifier
        ) {
            EnterPhoneNumberFragment.createNewInstance()
        }

        factory<SupportAppScreen>(
            enterPhoneNumberScreenQualifier
        ) {
            EnterPhoneNumberScreen()
        }

        factory<ConnectionStateSendChannel>(connectionStateEnterPhoneNumberSendChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateEnterPhoneNumberBroadcastChannelQualifier)
        }
        factory<ConnectionStateReceiveChannel>(connectionStateEnterPhoneNumberReceiveChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateEnterPhoneNumberBroadcastChannelQualifier)
                .openSubscription()
        }
        single<ConnectionStateBroadcastChannel>(connectionStateEnterPhoneNumberBroadcastChannelQualifier) {
            BroadcastChannel<TdApi.ConnectionState>(Channel.CONFLATED)
        }

        factory<EnterPhoneNumberPresenter>(enterPhoneNumberPresenterQualifier) {
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
