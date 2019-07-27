package com.anvipo.angram.dataLayer.di

import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.application.ApplicationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.application.ApplicationTDLibGatewayImp
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGatewayImp
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy.ProxyTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.proxy.ProxyTDLibGatewayImp
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GatewaysModule {

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

    }

}