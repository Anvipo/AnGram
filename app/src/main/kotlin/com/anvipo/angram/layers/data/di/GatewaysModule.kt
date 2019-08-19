package com.anvipo.angram.layers.data.di

import androidx.room.Room
import com.anvipo.angram.layers.application.launchSystem.App
import com.anvipo.angram.layers.core.IOScope
import com.anvipo.angram.layers.data.gateways.local.db.room.AppDatabase
import com.anvipo.angram.layers.data.gateways.local.db.room.proxy.ProxyRoomDAO
import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAOImpl
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGatewayImpl
import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGatewayImpl
import com.anvipo.angram.layers.data.gateways.tdLib.proxy.ProxyTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.proxy.ProxyTDLibGatewayImpl
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.KoinComponent
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GatewaysModule : CoroutineScope by IOScope(), KoinComponent {

    private val appDatabaseQualifier = named("appDatabase")

    val sharedPreferencesGatewayQualifier: StringQualifier = named("sharedPreferencesGateway")
    val proxyLocalGatewayQualifier: StringQualifier = named("proxyLocalGateway")

    val proxyTDLibGatewayQualifier: StringQualifier = named("proxyTDLibGateway")
    val applicationTDLibGatewayQualifier: StringQualifier = named("applicationTDLibGateway")
    val authorizationTDLibGatewayQualifier: StringQualifier = named("authorizationTDLibGateway")

    private val tdClientQualifier = named("tdClient")
    val tdClientScopeQualifier: StringQualifier = named("tdClientScope")

    private val tdLibUpdatesExceptionHandlerQualifier = named("updatesExceptionHandler")

    private val tdLibUpdatesHandlerQualifier = named("handleTDLibUpdate")

    private val tdLibDefaultExceptionHandlerQualifier = named("defaultExceptionHandler")

    @ExperimentalCoroutinesApi
    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        scope(tdClientScopeQualifier) {
            scoped<Client>(tdClientQualifier) {
                Client.create(
                    get(tdLibUpdatesHandlerQualifier),
                    get(tdLibUpdatesExceptionHandlerQualifier),
                    get(tdLibDefaultExceptionHandlerQualifier)
                )
            }

            scoped<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier) {
                ApplicationTDLibGatewayImpl(
                    tdLibClient = App.tdClientScope.get(tdClientQualifier),
                    resourceManager = get()
                )
            }
        }


        scope(authorizationCoordinatorScopeQualifier) {

            scoped<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier) {
                AuthorizationTDLibGatewayImpl(
                    tdLibClient = App.tdClientScope.get(tdClientQualifier)
                )
            }

        }


        factory<ProxyTDLibGateway>(proxyTDLibGatewayQualifier) {
            ProxyTDLibGatewayImpl(
                tdLibClient = App.tdClientScope.get(tdClientQualifier)
            )
        }


        single<Client.ResultHandler>(tdLibUpdatesHandlerQualifier) {
            val app = androidApplication() as App

            Client.ResultHandler(app::handleTDLibUpdate)
        }

        single<Client.ExceptionHandler>(tdLibUpdatesExceptionHandlerQualifier) {
            val app = androidApplication() as App

            Client.ExceptionHandler(app::handleTDLibUpdatesException)
        }

        single<Client.ExceptionHandler>(tdLibDefaultExceptionHandlerQualifier) {
            val app = androidApplication() as App

            Client.ExceptionHandler(app::handleTDLibDefaultException)
        }

        single<ProxyRoomDAO>(proxyLocalGatewayQualifier) {
            get<AppDatabase>(appDatabaseQualifier).proxyDao
        }

        single<SharedPreferencesDAO>(sharedPreferencesGatewayQualifier) {
            SharedPreferencesDAOImpl(
                resourceManager = get()
            )
        }

        single<AppDatabase>(appDatabaseQualifier) {
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