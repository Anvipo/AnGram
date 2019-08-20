package com.anvipo.angram.layers.application.launchSystem

import android.app.Application
import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule
import com.anvipo.angram.layers.application.tdApiHelper.TdApiHelper
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule
import com.anvipo.angram.layers.core.CoreHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.data.di.GatewaysModule
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : Application() {

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    @ExperimentalCoroutinesApi
    private fun initDI() {
        val modules by lazy {
            if (IS_IN_DEBUG_MODE) {
                listOf(
                    SystemInfrastructureModule.module,
                    TdApiHelperModule.module,
                    UseCasesModule.module,
                    GatewaysModule.module,
                    AppActivityModule.module,
                    ApplicationCoordinatorModule.module,
                    AuthorizationCoordinatorModule.module,
                    EnterAuthenticationPhoneNumberModule.module,
                    EnterAuthenticationCodeModule.module,
                    EnterAuthenticationPasswordModule.module,
                    AddProxyModule.module
                )
            } else {
                TODO("release config")
            }
        }

        startKoin {
            androidLogger()

            androidContext(this@App)

            modules(modules)
        }

        TdApiHelper.onCompletedDISetup()
    }

}
