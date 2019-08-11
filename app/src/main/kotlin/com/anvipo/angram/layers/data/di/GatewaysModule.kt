package com.anvipo.angram.layers.data.di

import androidx.room.Room
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.application.launchSystem.App
import com.anvipo.angram.layers.data.gateways.local.db.room.AppDatabase
import com.anvipo.angram.layers.data.gateways.local.db.room.proxy.ProxyRoomDAO
import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.layers.data.gateways.local.sharedPreferences.SharedPreferencesDAOImp
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGatewayImp
import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGatewayImp
import com.anvipo.angram.layers.data.gateways.tdLib.proxy.ProxyTDLibGateway
import com.anvipo.angram.layers.data.gateways.tdLib.proxy.ProxyTDLibGatewayImp
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GatewaysModule {

    private val appDatabaseQualifier = named("appDatabase")

    val sharedPreferencesGatewayQualifier: StringQualifier = named("sharedPreferencesGateway")
    val proxyLocalGatewayQualifier: StringQualifier = named("proxyLocalGateway")

    val proxyTDLibGatewayQualifier: StringQualifier = named("proxyTDLibGateway")
    val applicationTDLibGatewayQualifier: StringQualifier = named("applicationTDLibGateway")
    val authorizationTDLibGatewayQualifier: StringQualifier = named("authorizationTDLibGateway")

    private val tdClientQualifier = named("tdClient")

    private val updatesExceptionHandlerQualifier = named("updatesExceptionHandler")

    private val updatesHandlerQualifier = named("handleUpdates")

    private val defaultExceptionHandlerQualifier = named("defaultExceptionHandler")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        // ----------------------------- TG -------------------------

        single<Client.ResultHandler>(updatesHandlerQualifier) {
            val app = androidApplication() as App

            Client.ResultHandler(app::handleUpdates)
        }

        single<Client.ExceptionHandler>(updatesExceptionHandlerQualifier) {
            val app = androidApplication() as App

            Client.ExceptionHandler(app::handleUpdatesException)
        }

        single<Client.ExceptionHandler>(defaultExceptionHandlerQualifier) {
            val app = androidApplication() as App

            Client.ExceptionHandler(app::handleDefaultException)
        }

        single<Client>(tdClientQualifier) {
            Client.create(
                get(updatesHandlerQualifier),
                get(updatesExceptionHandlerQualifier),
                get(defaultExceptionHandlerQualifier)
            )
        }

        // ----------------------------- TG -------------------------


        // ---------------------------- GATEWAYS ---------------------

        factory<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier) {
            ApplicationTDLibGatewayImp(
                tdClient = get(tdClientQualifier)
            )
        }

        factory<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier) {
            AuthorizationTDLibGatewayImp(
                tdClient = get(tdClientQualifier)
            )
        }

        factory<ProxyTDLibGateway>(proxyTDLibGatewayQualifier) {
            ProxyTDLibGatewayImp(
                tdClient = get(tdClientQualifier)
            )
        }

        single<ProxyRoomDAO>(proxyLocalGatewayQualifier) {
            get<AppDatabase>(appDatabaseQualifier).proxyDao
        }

        single<SharedPreferencesDAO>(sharedPreferencesGatewayQualifier) {
            SharedPreferencesDAOImp(
                resourceManager = get(resourceManagerQualifier)
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