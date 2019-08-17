package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.enterAuthenticationCodeUseCaseQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModelImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment
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
        override fun getFragment(): Fragment =
            get(enterAuthenticationCodeViewQualifier) {
                parametersOf(parameters)
            }
    }

    val enterAuthenticationCodeScreenFactoryQualifier: StringQualifier = named("enterAuthenticationCodeScreenFactory")

    val enterAuthenticationCodeViewQualifier: StringQualifier = named("enterAuthenticationCodeView")
    val enterAuthenticationCodeScreenQualifier: StringQualifier = named("enterAuthenticationCodeScreen")

    val enterAuthenticationCodePresenterQualifier: StringQualifier = named("enterAuthenticationCodePresenter")

    class EnterAuthenticationCodeScreenParameters(
        val shouldShowBackButton: Boolean,
        val expectedCodeLength: Int,
        val enteredPhoneNumber: String,
        val registrationRequired: Boolean,
        val termsOfServiceText: String
    )

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnterAuthenticationCodeScreenFactory>(
            enterAuthenticationCodeScreenFactoryQualifier
        ) {
            EnterAuthenticationCodeScreenFactoryImpl(
                koinScope = this
            )
        }

        factory<EnterAuthenticationCodeView>(
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

        factory<EnterAuthenticationCodeViewModel>(enterAuthenticationCodePresenterQualifier) {
            EnterAuthenticationCodeViewModelImpl(
                routeEventHandler = get(authorizationCoordinatorQualifier),
                useCase = get(enterAuthenticationCodeUseCaseQualifier),
                resourceManager = get(resourceManagerQualifier)
            )
        }

    }

}