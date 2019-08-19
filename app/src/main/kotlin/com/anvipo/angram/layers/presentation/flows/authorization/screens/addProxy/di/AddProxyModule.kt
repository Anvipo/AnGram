package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.di

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.addProxyUseCaseQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy.AddProxyScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.AddProxyFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModelImpl
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AddProxyModule {

    class AddProxyScreen(
        private val parameters: AddProxyScreenParameters
    ) : SupportAppScreen(), KoinComponent {
        @SuppressLint("SyntheticAccessor")
        override fun getFragment(): Fragment =
            get(addProxyViewQualifier) {
                parametersOf(parameters)
            }
    }

    class AddProxyScreenParameters(
        val shouldShowBackButton: Boolean,
        val proxyType: ProxyType
    )

    val addProxyScreenFactoryQualifier: StringQualifier = named("addProxyScreenFactory")

    private val addProxyViewQualifier: StringQualifier = named("addProxyView")
    val addProxyScreenQualifier: StringQualifier = named("addProxyScreen")

    private val addProxyViewModelQualifier: StringQualifier = named("addProxyViewModel")
    val addProxyViewModelFactoryQualifier: StringQualifier = named("addProxyViewModelFactory")

    private object AddProxyViewModelFactory : ViewModelProvider.NewInstanceFactory(), KoinComponent {
        @SuppressLint("SyntheticAccessor")
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return get<AddProxyViewModel>(addProxyViewModelQualifier) as T
        }
    }

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<AddProxyScreenFactory>(
            addProxyScreenFactoryQualifier
        ) {
            AddProxyScreenFactoryImpl(
                koinScope = this
            )
        }

        factory<AddProxyFragment>(
            addProxyViewQualifier
        ) { (parameters: AddProxyScreenParameters) ->
            AddProxyFragment.createNewInstance(
                shouldShowBackButton = parameters.shouldShowBackButton,
                proxyType = parameters.proxyType
            )
        }

        factory<SupportAppScreen>(
            addProxyScreenQualifier
        ) { (parameters: AddProxyScreenParameters) ->
            AddProxyScreen(parameters = parameters)
        }

        single<AddProxyViewModelFactory>(addProxyViewModelFactoryQualifier) {
            AddProxyViewModelFactory
        }


        factory<AddProxyViewModel>(addProxyViewModelQualifier) {
            AddProxyViewModelImpl(
                routeEventHandler = authorizationCoordinatorScope.get(authorizationCoordinatorQualifier),
                useCase = get(addProxyUseCaseQualifier),
                resourceManager = get(resourceManagerQualifier)
            )
        }

    }

}