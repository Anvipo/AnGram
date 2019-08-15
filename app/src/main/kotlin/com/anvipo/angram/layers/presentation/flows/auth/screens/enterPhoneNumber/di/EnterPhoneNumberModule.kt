package com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.enterPhoneNumberUseCaseQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionState
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateSendChannel
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactoryImp
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.view.EnterPhoneNumberView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterPhoneNumberModule {

    class EnterPhoneNumberScreen : SupportAppScreen(), KoinComponent {
        override fun getFragment(): Fragment = get(enterPhoneNumberViewQualifier)
    }

    val enterPhoneNumberScreenFactoryQualifier: StringQualifier =
        named("enterPhoneNumberScreenFactory")
    val enterPhoneNumberViewQualifier: StringQualifier =
        named("enterPhoneNumberView")

    val enterPhoneNumberScreenQualifier: StringQualifier =
        named("enterPhoneNumberScreen")

    val enterPhoneNumberPresenterQualifier: StringQualifier =
        named("enterPhoneNumberPresenter")

    private val tdApiUpdateConnectionStateEnterPhoneNumberScreenReceiveChannelQualifier =
        named("tdApiUpdateConnectionStateEnterPhoneNumberScreenReceiveChannel")
    val tdApiUpdateConnectionStateEnterPhoneNumberScreenSendChannelQualifier: StringQualifier =
        named("tdApiUpdateConnectionStateEnterPhoneNumberScreenSendChannel")
    private val tdApiUpdateConnectionStateEnterPhoneNumberScreenBroadcastChannelQualifier =
        named("tdApiUpdateConnectionStateEnterPhoneNumberScreenBroadcastChannel")

    @ExperimentalCoroutinesApi
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

        factory<TdApiUpdateConnectionStateSendChannel>(
            tdApiUpdateConnectionStateEnterPhoneNumberScreenSendChannelQualifier
        ) {
            get(tdApiUpdateConnectionStateEnterPhoneNumberScreenBroadcastChannelQualifier)
        }
        factory<TdApiUpdateConnectionStateReceiveChannel>(
            tdApiUpdateConnectionStateEnterPhoneNumberScreenReceiveChannelQualifier
        ) {
            get<TdApiUpdateConnectionStateBroadcastChannel>(
                tdApiUpdateConnectionStateEnterPhoneNumberScreenBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateConnectionStateBroadcastChannel>(
            tdApiUpdateConnectionStateEnterPhoneNumberScreenBroadcastChannelQualifier
        ) {
            BroadcastChannel<TdApiUpdateConnectionState>(Channel.CONFLATED)
        }

        factory<EnterPhoneNumberPresenter>(enterPhoneNumberPresenterQualifier) {
            EnterPhoneNumberPresenterImp(
                routeEventHandler = get(authorizationCoordinatorQualifier),
                useCase = get(enterPhoneNumberUseCaseQualifier),
                resourceManager = get(resourceManagerQualifier),
                tdApiUpdateConnectionStateReceiveChannel = get(
                    tdApiUpdateConnectionStateEnterPhoneNumberScreenReceiveChannelQualifier
                )
            )
        }

    }

}
