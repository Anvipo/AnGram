package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModelImpl
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthenticationPasswordModule {

    class EnterPasswordScreen : SupportAppScreen(), KoinComponent {
        @SuppressLint("SyntheticAccessor")
        override fun getFragment(): Fragment =
            get(enterAuthenticationPasswordViewQualifier)
    }

    val enterAuthenticationPasswordScreenFactoryQualifier: StringQualifier =
        named("enterAuthenticationPasswordScreenFactory")

    private val enterAuthenticationPasswordViewQualifier: StringQualifier =
        named("enterAuthenticationPasswordView")
    val enterAuthenticationPasswordScreenQualifier: StringQualifier =
        named("enterAuthenticationPasswordScreen")

    private val enterAuthenticationPasswordViewModelQualifier: StringQualifier =
        named("enterAuthenticationPasswordViewModel")

    val enterAuthenticationPasswordViewModelFactoryQualifier: StringQualifier =
        named("enterAuthenticationPasswordViewModelFactory")

    private object EnterAuthenticationPasswordViewModelFactory : ViewModelProvider.NewInstanceFactory(), KoinComponent {
        @SuppressLint("SyntheticAccessor")
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return get<EnterAuthenticationPasswordViewModel>(
                enterAuthenticationPasswordViewModelQualifier
            ) as T
        }
    }

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnterAuthenticationPasswordScreenFactory>(enterAuthenticationPasswordScreenFactoryQualifier) {
            EnterAuthenticationPasswordScreenFactoryImpl(
                koinScope = this
            )
        }

        factory<EnterAuthenticationPasswordFragment>(
            enterAuthenticationPasswordViewQualifier
        ) {
            EnterAuthenticationPasswordFragment.createNewInstance()
        }

        factory<SupportAppScreen>(
            enterAuthenticationPasswordScreenQualifier
        ) {
            EnterPasswordScreen()
        }

        single<EnterAuthenticationPasswordViewModelFactory>(
            enterAuthenticationPasswordViewModelFactoryQualifier
        ) {
            EnterAuthenticationPasswordViewModelFactory
        }

        factory<EnterAuthenticationPasswordViewModel>(enterAuthenticationPasswordViewModelQualifier) {
            EnterAuthenticationPasswordViewModelImpl(
                routeEventHandler = authorizationCoordinatorScope.get(authorizationCoordinatorQualifier),
                useCase = authorizationCoordinatorScope.get(),
                resourceManager = get()
            )
        }

    }

}