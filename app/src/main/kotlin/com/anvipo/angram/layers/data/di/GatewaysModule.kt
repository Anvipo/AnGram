package com.anvipo.angram.layers.data.di

import androidx.room.Room
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.application.launchSystem.App
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import com.anvipo.angram.layers.core.IOScope
import com.anvipo.angram.layers.core.logHelpers.CoroutineExceptionHandlerWithLogger
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
import com.anvipo.angram.layers.global.types.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.KoinComponent
import org.koin.core.get
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

    private val tdLibUpdatesExceptionHandlerQualifier = named("updatesExceptionHandler")

    private val tdLibUpdatesHandlerQualifier = named("handleTDLibUpdate")

    private val tdLibDefaultExceptionHandlerQualifier = named("defaultExceptionHandler")

    val mustRecreateTDLibClientSendChannelQualifier: StringQualifier =
        named("mustRecreateTDLibClientSendChannel")
    private val mustRecreateTDLibClientReceiveChannelQualifier =
        named("mustRecreateTDLibClientReceiveChannel")
    private val mustRecreateTDLibClientBroadcastChannelQualifier =
        named("mustRecreateTDLibClientBroadcastChannel")

    val tdLibClientHasBeenRecreatedReceiveChannelQualifier: StringQualifier =
        named("tdLibClientHasBeenRecreatedReceiveChannel")
    private val tdLibClientHasBeenRecreatedSendChannelQualifier =
        named("tdLibClientHasBeenRecreatedSendChannel")
    private val tdLibClientHasBeenRecreatedBroadcastChannelQualifier =
        named("tdLibClientHasBeenRecreatedBroadcastChannel")

    private val startListenQualifier = named("startListen")

    private var client: Client? = null

    private val mustRecreateTDLibClientReceiveChannel by lazy {
        get<MustRecreateTDLibClientReceiveChannel>(
            mustRecreateTDLibClientReceiveChannelQualifier
        )
    }

    private val tdLibClientHasBeenRecreatedSendChannel by lazy {
        get<TDLibClientHasBeenRecreatedSendChannel>(
            tdLibClientHasBeenRecreatedSendChannelQualifier
        )
    }

    @ExperimentalCoroutinesApi
    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<TDLibClientHasBeenRecreatedSendChannel>(
            tdLibClientHasBeenRecreatedSendChannelQualifier
        ) {
            get(tdLibClientHasBeenRecreatedBroadcastChannelQualifier)
        }
        factory<TDLibClientHasBeenRecreatedReceiveChannel>(
            tdLibClientHasBeenRecreatedReceiveChannelQualifier
        ) {
            get<TDLibClientHasBeenRecreatedBroadcastChannel>(
                tdLibClientHasBeenRecreatedBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TDLibClientHasBeenRecreatedBroadcastChannel>(
            tdLibClientHasBeenRecreatedBroadcastChannelQualifier
        ) {
            BroadcastChannel<TDLibClientHasBeenRecreated>(Channel.CONFLATED)
        }

        single<MustRecreateTDLibClientSendChannel>(
            mustRecreateTDLibClientSendChannelQualifier
        ) {
            get(mustRecreateTDLibClientBroadcastChannelQualifier)
        }
        factory<MustRecreateTDLibClientReceiveChannel>(
            mustRecreateTDLibClientReceiveChannelQualifier
        ) {
            get<MustRecreateTDLibClientBroadcastChannel>(
                mustRecreateTDLibClientBroadcastChannelQualifier
            ).openSubscription()
        }
        single<MustRecreateTDLibClientBroadcastChannel>(
            mustRecreateTDLibClientBroadcastChannelQualifier
        ) {
            BroadcastChannel<MustRecreateTDLibClient>(Channel.CONFLATED)
        }

        // ----------------------------- TG -------------------------

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

        single<Unit>(startListenQualifier) {
            val invokationPlace = object {}.javaClass.enclosingMethod!!.name

            val coroutineExceptionHandlerWithLogger =
                CoroutineExceptionHandlerWithLogger { _, throwable ->
                    logIfShould(
                        text = throwable.message,
                        invokationPlace = invokationPlace
                    )
                }

            launch(coroutineExceptionHandlerWithLogger) {
                for (mustRecreateTDLibClientPing in mustRecreateTDLibClientReceiveChannel) {
                    client = Client.create(
                        get(tdLibUpdatesHandlerQualifier),
                        get(tdLibUpdatesExceptionHandlerQualifier),
                        get(tdLibDefaultExceptionHandlerQualifier)
                    )

                    tdLibClientHasBeenRecreatedSendChannel.send(TDLibClientHasBeenRecreated)
                }
            }
        }

        factory<Client>(tdClientQualifier) {
            if (client == null) {
                // for first app's launch
                client = Client.create(
                    get(tdLibUpdatesHandlerQualifier),
                    get(tdLibUpdatesExceptionHandlerQualifier),
                    get(tdLibDefaultExceptionHandlerQualifier)
                )

                get<Unit>(startListenQualifier)
            }

            client!!
        }

        // ----------------------------- TG -------------------------


        // ---------------------------- GATEWAYS ---------------------

        factory<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier) {
            ApplicationTDLibGatewayImp(
                tdLibClient = get(tdClientQualifier),
                resourceManager = get(resourceManagerQualifier)
            )
        }

        factory<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier) {
            AuthorizationTDLibGatewayImp(
                tdLibClient = get(tdClientQualifier)
            )
        }

        factory<ProxyTDLibGateway>(proxyTDLibGatewayQualifier) {
            ProxyTDLibGatewayImp(
                tdLibClient = get(tdClientQualifier)
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

    fun stopCoroutinesWork() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("${this::class.java.name}::$methodName")

        mustRecreateTDLibClientReceiveChannel.cancel(cancellationException)

        try {
            cancel(cancellationException)
        } catch (exception: Exception) {
            logIfShould(
                invokationPlace = methodName,
                text = "exception = $exception"
            )
        }
    }

}