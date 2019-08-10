package com.anvipo.angram.dataLayer.di

import androidx.room.Room
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.dataLayer.gateways.local.db.room.AppDatabase
import com.anvipo.angram.dataLayer.gateways.local.db.room.proxy.ProxyRoomDAO
import com.anvipo.angram.dataLayer.gateways.local.sharedPreferences.SharedPreferencesDAO
import com.anvipo.angram.dataLayer.gateways.local.sharedPreferences.SharedPreferencesDAOImp
import com.anvipo.angram.dataLayer.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.application.ApplicationTDLibGatewayImp
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGatewayImp
import com.anvipo.angram.dataLayer.gateways.tdLib.proxy.ProxyTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.proxy.ProxyTDLibGatewayImp
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GatewaysModule {

    private val appDatabaseQualifier = named("appDatabase")

    internal val sharedPreferencesGatewayQualifier = named("sharedPreferencesGateway")
    internal val proxyLocalGatewayQualifier = named("proxyLocalGateway")

    internal val proxyTDLibGatewayQualifier = named("proxyTDLibGateway")
    internal val applicationTDLibGatewayQualifier = named("applicationTDLibGateway")
    internal val authorizationTDLibGatewayQualifier = named("authorizationTDLibGateway")

    private val tdClientQualifier = named("tdClient")

    private val updatesExceptionHandlerQualifier = named("updatesExceptionHandler")

    private val updatesHandlerQualifier = named("updatesHandler")

    private val defaultExceptionHandlerQualifier = named("defaultExceptionHandler")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        // ----------------------------- TG -------------------------

        single<Client.ResultHandler>(updatesHandlerQualifier) {
            val app = androidApplication() as App
            val updatesHandlerFunction = app.updatesHandlerFunction

            Client.ResultHandler(updatesHandlerFunction)
        }

        single<Client.ExceptionHandler>(updatesExceptionHandlerQualifier) {
            val app = androidApplication() as App
            val updatesExceptionHandlerFunction = app.updatesExceptionHandlerFunction

            Client.ExceptionHandler(updatesExceptionHandlerFunction)
        }

        single<Client.ExceptionHandler>(defaultExceptionHandlerQualifier) {
            val app = androidApplication() as App
            val defaultExceptionHandlerFunction = app.defaultExceptionHandlerFunction

            Client.ExceptionHandler(defaultExceptionHandlerFunction)
        }

        single<Client>(tdClientQualifier) {
            Client.create(
                get<Client.ResultHandler>(updatesHandlerQualifier),
                get<Client.ExceptionHandler>(updatesExceptionHandlerQualifier),
                get<Client.ExceptionHandler>(defaultExceptionHandlerQualifier)
            )
        }

        // ----------------------------- TG -------------------------


        // ---------------------------- GATEWAYS ---------------------

        single<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier) {
            ApplicationTDLibGatewayImp(
                tdClient = get<Client>(tdClientQualifier)
            )
        }

        single<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier) {
            AuthorizationTDLibGatewayImp(
                tdClient = get<Client>(tdClientQualifier)
            )
        }

        single<ProxyTDLibGateway>(proxyTDLibGatewayQualifier) {
            ProxyTDLibGatewayImp(
                tdClient = get<Client>(tdClientQualifier)
            )
        }

        single<ProxyRoomDAO>(proxyLocalGatewayQualifier) {
            get<AppDatabase>(appDatabaseQualifier).proxyDao
        }

        single<SharedPreferencesDAO>(sharedPreferencesGatewayQualifier) {
            SharedPreferencesDAOImp(
                resourceManager = get<ResourceManager>(
                    resourceManagerQualifier
                )
            )
        }

        single<AppDatabase>(appDatabaseQualifier) {
            Room
                .databaseBuilder(
                    get(),
                    AppDatabase::class.java,
                    "angram-db"
                )
                .build()
        }

    }

}