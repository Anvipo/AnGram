package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModelImpl
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthenticationCodeModule {

    class EnterAuthenticationCodeScreen(
        private val parameters: EnterAuthenticationCodeScreenParameters
    ) : SupportAppScreen(), KoinComponent {
        @SuppressLint("SyntheticAccessor")
        override fun getFragment(): Fragment =
            get(enterAuthenticationCodeViewQualifier) {
                parametersOf(parameters)
            }
    }

    val enterAuthenticationCodeScreenFactoryQualifier: StringQualifier = named("enterAuthenticationCodeScreenFactory")

    private val enterAuthenticationCodeViewQualifier: StringQualifier = named("enterAuthenticationCodeView")
    val enterAuthenticationCodeScreenQualifier: StringQualifier = named("enterAuthenticationCodeScreen")

    private val enterAuthenticationCodeViewModelQualifier: StringQualifier =
        named("enterAuthenticationCodeViewModel")

    class EnterAuthenticationCodeScreenParameters(
        val shouldShowBackButton: Boolean,
        val expectedCodeLength: Int,
        val enteredPhoneNumber: String,
        val registrationRequired: Boolean,
        val termsOfServiceText: String
    )

    val enterAuthenticationCodeViewModelFactoryQualifier: StringQualifier =
        named("enterAuthenticationCodeViewModelFactory")

    private object EnterAuthenticationCodeViewModelFactory :
        ViewModelProvider.Factory, KoinComponent {
        @SuppressLint("SyntheticAccessor")
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return get<EnterAuthenticationCodeViewModel>(enterAuthenticationCodeViewModelQualifier) as T
        }
    }

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnterAuthenticationCodeScreenFactory>(
            enterAuthenticationCodeScreenFactoryQualifier
        ) {
            EnterAuthenticationCodeScreenFactoryImpl(
                koinScope = this
            )
        }

        factory<EnterAuthenticationCodeFragment>(
            enterAuthenticationCodeViewQualifier
        ) { (parameters: EnterAuthenticationCodeScreenParameters) ->
            EnterAuthenticationCodeFragment.createNewInstance(
                shouldShowBackButton = parameters.shouldShowBackButton,
                expectedCodeLength = parameters.expectedCodeLength,
                enteredPhoneNumber = parameters.enteredPhoneNumber,
                registrationRequired = parameters.registrationRequired,
                termsOfServiceText = parameters.termsOfServiceText
            )
        }

        factory<SupportAppScreen>(
            enterAuthenticationCodeScreenQualifier
        ) { (parameters: EnterAuthenticationCodeScreenParameters) ->
            EnterAuthenticationCodeScreen(parameters = parameters)
        }

        single<EnterAuthenticationCodeViewModelFactory>(enterAuthenticationCodeViewModelFactoryQualifier) {
            EnterAuthenticationCodeViewModelFactory
        }

        factory<EnterAuthenticationCodeViewModel>(enterAuthenticationCodeViewModelQualifier) {
            EnterAuthenticationCodeViewModelImpl(
                routeEventHandler = authorizationCoordinatorScope.get(authorizationCoordinatorQualifier),
                useCase = authorizationCoordinatorScope.get(),
                resourceManager = get()
            )
        }

    }

}