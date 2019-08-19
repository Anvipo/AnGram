package com.anvipo.angram.layers.data.di

import androidx.room.Room
import com.anvipo.angram.layers.application.launchSystem.App
import com.anvipo.angram.layers.data.gateways.local.db.room.AppDatabase
import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAOImpl
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGatewayImpl
import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGatewayImpl
import com.anvipo.angram.layers.data.gateways.tdLib.proxy.ProxyTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.proxy.ProxyTDLibGatewayImpl
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GatewaysModule {

    val tdClientScopeQualifier: StringQualifier = named("tdClientScope")

    private val tdLibUpdatesExceptionHandlerQualifier = named("updatesExceptionHandler")

    private val tdLibDefaultExceptionHandlerQualifier = named("defaultExceptionHandler")

    @ExperimentalCoroutinesApi
    val module: Module = module {

        scope(tdClientScopeQualifier) {

            scoped {
                Client.create(
                    get(),
                    get(tdLibUpdatesExceptionHandlerQualifier),
                    get(tdLibDefaultExceptionHandlerQualifier)
                )
            }

            scoped<ApplicationTDLibGateway> {
                ApplicationTDLibGatewayImpl(
                    tdLibClient = App.tdClientScope.get(),
                    resourceManager = get()
                )
            }

        }


        scope(authorizationCoordinatorScopeQualifier) {

            scoped<AuthorizationTDLibGateway> {
                AuthorizationTDLibGatewayImpl(
                    tdLibClient = App.tdClientScope.get()
                )
            }

        }


        factory<ProxyTDLibGateway> {
            ProxyTDLibGatewayImpl(
                tdLibClient = App.tdClientScope.get()
            )
        }


        single {
            val app = androidApplication() as App

            Client.ResultHandler(app::handleTDLibUpdate)
        }

        single(tdLibUpdatesExceptionHandlerQualifier) {
            val app = androidApplication() as App

            Client.ExceptionHandler(app::handleTDLibUpdatesException)
        }

        single(tdLibDefaultExceptionHandlerQualifier) {
            val app = androidApplication() as App

            Client.ExceptionHandler(app::handleTDLibDefaultException)
        }

        single {
            get<AppDatabase>().proxyDao
        }

        single<SharedPreferencesDAO> {
            SharedPreferencesDAOImpl(
                resourceManager = get()
            )
        }

        single {
            Room
                .databaseBuilder(
                    androidApplication().applicationContext,
                    AppDatabase::class.java,
                    "angram-db"
                )
                .build()
        }

    }

}