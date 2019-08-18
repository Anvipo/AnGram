package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.enterAuthenticationPhoneNumberUseCaseQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionState
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateSendChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.EnterAuthenticationPhoneNumberFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModelImpl
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
        @SuppressLint("SyntheticAccessor")
        override fun getFragment(): Fragment =
            get(enterAuthenticationPhoneNumberViewQualifier)
    }

    val enterAuthenticationPhoneNumberScreenFactoryQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberScreenFactory")
    val enterAuthenticationPhoneNumberScreenQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberScreen")

    private val enterAuthenticationPhoneNumberViewQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberView")

    private val enterAuthenticationPhoneNumberViewModelQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberViewModel")

    private val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannelQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenReceiveChannel")
    val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier: StringQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannel")
    private val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannelQualifier =
        named("tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenBroadcastChannel")

    val enterAuthenticationPhoneNumberViewModelFactoryQualifier: StringQualifier =
        named("enterAuthenticationPhoneNumberViewModelFactory")

    private object EnterAuthenticationPhoneNumberViewModelFactory :
        ViewModelProvider.NewInstanceFactory(),
        KoinComponent {
        @SuppressLint("SyntheticAccessor")
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            get<EnterAuthenticationPhoneNumberViewModel>(
                enterAuthenticationPhoneNumberViewModelQualifier
            ) as T
    }

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

        factory<EnterAuthenticationPhoneNumberFragment>(
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

        single<EnterAuthenticationPhoneNumberViewModelFactory>(
            enterAuthenticationPhoneNumberViewModelFactoryQualifier
        ) {
            EnterAuthenticationPhoneNumberViewModelFactory
        }

        factory<EnterAuthenticationPhoneNumberViewModel>(
            enterAuthenticationPhoneNumberViewModelQualifier
        ) {
            EnterAuthenticationPhoneNumberViewModelImpl(
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
