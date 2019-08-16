package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.enterAuthenticationPhoneNumberUseCaseQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionState
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateSendChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.presenter.EnterAuthenticationPhoneNumberPresenter
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.presenter.EnterAuthenticationPhoneNumberPresenterImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.EnterAuthenticationPhoneNumberFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.EnterAuthenticationPhoneNumberView
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

object EnterAuthenticationPhoneNumberModule {

    class EnterAuthenticationPhoneNumberScreen : SupportAppScreen(), KoinComponent {
        override fun getFragment(): Fragment = get(enterAuthenticationPhoneNumberViewQualifier)
    }

    val enterAuthenticationPhoneNumberScreenFactoryQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberScreenFactory")
    val enterAuthenticationPhoneNumberViewQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberView")

    val enterAuthenticationPhoneNumberScreenQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberScreen")

    val enterAuthenticationPhoneNumberPresenterQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberPresenter")

    private val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannelQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannel")
    val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier: StringQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannel")
    private val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannel")

    @ExperimentalCoroutinesApi
    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnterAuthenticationPhoneNumberScreenFactory>(
            enterAuthenticationPhoneNumberScreenFactoryQualifier
        ) {
            EnterAuthenticationPhoneNumberScreenFactoryImpl(
                koinScope = this
            )
        }

        factory<EnterAuthenticationPhoneNumberView>(
            enterAuthenticationPhoneNumberViewQualifier
        ) {
            EnterAuthenticationPhoneNumberFragment.createNewInstance()
        }

        factory<SupportAppScreen>(
            enterAuthenticationPhoneNumberScreenQualifier
        ) {
            EnterAuthenticationPhoneNumberScreen()
        }

        factory<TdApiUpdateConnectionStateSendChannel>(
            tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier
        ) {
            get(tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier)
        }
        factory<TdApiUpdateConnectionStateReceiveChannel>(
            tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannelQualifier
        ) {
            get<TdApiUpdateConnectionStateBroadcastChannel>(
                tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateConnectionStateBroadcastChannel>(
            tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier
        ) {
            BroadcastChannel<TdApiUpdateConnectionState>(Channel.CONFLATED)
        }

        factory<EnterAuthenticationPhoneNumberPresenter>(
            enterAuthenticationPhoneNumberPresenterQualifier
        ) {
            EnterAuthenticationPhoneNumberPresenterImpl(
                routeEventHandler = get(authorizationCoordinatorQualifier),
                useCase = get(enterAuthenticationPhoneNumberUseCaseQualifier),
                resourceManager = get(resourceManagerQualifier),
                tdApiUpdateConnectionStateReceiveChannel = get(
                    tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannelQualifier
                )
            )
        }

    }

}
