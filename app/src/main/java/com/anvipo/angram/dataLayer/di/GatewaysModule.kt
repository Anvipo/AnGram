package com.anvipo.angram.dataLayer.di

import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGatewayImp
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GatewaysModule {

    internal val tdLibGateway: StringQualifier = named("tdLibGateway")

    private val tdClient: StringQualifier = named("tdClient")

    private val updatesExceptionHandler: StringQualifier = named("updatesExceptionHandler")

    private val updatesHandler: StringQualifier = named("updatesHandler")

    private val defaultExceptionHandler: StringQualifier = named("defaultExceptionHandler")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        // ----------------------------- TG -------------------------

        single<Client.ResultHandler>(updatesHandler) {
            val app = androidApplication() as App
            val updatesHandlerFunction = app.updatesHandlerFunction

            Client.ResultHandler(updatesHandlerFunction)
        }

        single<Client.ExceptionHandler>(updatesExceptionHandler) {
            val app = androidApplication() as App
            val updatesExceptionHandlerFunction = app.updatesExceptionHandlerFunction

            Client.ExceptionHandler(updatesExceptionHandlerFunction)
        }

        single<Client.ExceptionHandler>(defaultExceptionHandler) {
            val app = androidApplication() as App
            val defaultExceptionHandlerFunction = app.defaultExceptionHandlerFunction

            Client.ExceptionHandler(defaultExceptionHandlerFunction)
        }

        single<Client>(tdClient) {
            Client.create(
                get<Client.ResultHandler>(updatesHandler),
                get<Client.ExceptionHandler>(updatesExceptionHandler),
                get<Client.ExceptionHandler>(defaultExceptionHandler)
            )
        }

        // ----------------------------- TG -------------------------


        // ---------------------------- GATEWAYS ---------------------

        single<TDLibGateway>(tdLibGateway) {
            TDLibGatewayImp(
                tdClient = get<Client>(tdClient)
            )
        }

    }

}