package com.anvipo.angram.applicationLayer.launchSystem

import android.app.Application
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.connectionStateAppSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.enabledProxyIdSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule
import com.anvipo.angram.applicationLayer.types.ConnectionStateSendChannel
import com.anvipo.angram.applicationLayer.types.EnabledProxyIdSendChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule
import com.anvipo.angram.coreLayer.CoreErrors
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.collections.IMutableStack
import com.anvipo.angram.coreLayer.collections.MutableStack
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.coreLayer.message.SystemMessageType
import com.anvipo.angram.dataLayer.di.GatewaysModule
import com.anvipo.angram.global.GlobalHelpers.createTGSystemMessageFromApp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di.EnterPhoneNumberModule.connectionStateEnterPhoneNumberSendChannelQualifier
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.error.*

class App : Application() {

    companion object {
        const val TAG: String = "AnGram"

        private lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        INSTANCE = this
    }

    internal val updatesHandlerFunction: (TdApi.Object) -> Unit = { tdApiObject ->
        tdObjectsStack.push(tdApiObject)

        val tag = "${this::class.java.simpleName} updatesHandler"
        val text = "$tag = $tdApiObject"

        debugLog(text)
        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))

        when (tdApiObject) {
            is TdApi.Update -> onUpdate(tdApiObject)
            else -> debugLog("Unhandled TdApi.Object case")
        }
    }

    internal val updatesExceptionHandlerFunction: (Throwable) -> Unit = { error ->
        tdErrorsStack.push(error)

        val tag = "${this::class.java.simpleName} updatesExceptionHandlerFunction"
        val text = "$tag = ${error.localizedMessage}"

        debugLog(text)

        when (error) {
            is NoBeanDefFoundException,
            is BadScopeInstanceException,
            is DefinitionOverrideException,
            is KoinAppAlreadyStartedException,
            is MissingPropertyException,
            is NoParameterFoundException,
            is NoPropertyFileFoundException,
            is NoScopeDefinitionFoundException,
            is ScopeAlreadyCreatedException,
            is ScopeNotCreatedException,
            is InstanceCreationException -> {
                val koinExceptionMessage = SystemMessage(
                    text = text,
                    type = SystemMessageType.ALERT,
                    shouldBeShownToUser = BuildConfig.DEBUG,
                    shouldBeShownInLogs = false
                )

                systemMessageSendChannel.offer(koinExceptionMessage)
            }
            is CoreErrors.DebugError -> systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            else -> systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
        }
    }

    internal val defaultExceptionHandlerFunction: (Throwable) -> Unit = { error ->
        tdErrorsStack.push(error)

        val tag = "${this::class.java.simpleName} defaultExceptionHandlerFunction"
        val text = "$tag = ${error.localizedMessage}"

        debugLog(text)
        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
    }


    private fun initDI() {
        val debugModules = listOf(
            LaunchSystemModule.module,
            SystemInfrastructureModule.module,
            UseCasesModule.module,
            GatewaysModule.module,
            ApplicationCoordinatorModule.module,
            EnterPhoneNumberModule.module,
            EnterAuthenticationCodeModule.module,
            EnterAuthenticationPasswordModule.module,
            AddProxyModule.module
        )

        if (BuildConfig.DEBUG) {
            startKoin {
                androidLogger()

                androidContext(this@App)

                modules(debugModules)
            }
        } else {
            // TODO: release config
            debugLog("TODO")
        }
    }

    private val systemMessageSendChannel: SystemMessageSendChannel
            by inject(systemMessageSendChannelQualifier)

    private val enabledProxyIdSendChannel: EnabledProxyIdSendChannel
            by inject(enabledProxyIdSendChannelQualifier)

    private val connectionStateAppSendChannel: ConnectionStateSendChannel
            by inject(connectionStateAppSendChannelQualifier)

    private val connectionStateEnterPhoneNumberSendChannel: ConnectionStateSendChannel
            by inject(connectionStateEnterPhoneNumberSendChannelQualifier)


    // ------- TG Client properties and methods


    private val tdObjectsStack: IMutableStack<TdApi.Object> = MutableStack()

    private val tdUpdateStack: IMutableStack<TdApi.Update> = MutableStack()

    private val tdErrorsStack: IMutableStack<Throwable> = MutableStack()


    private fun onUpdate(
        tdApiUpdate: TdApi.Update
    ) {
        tdUpdateStack.push(tdApiUpdate)

        when (tdApiUpdate) {
            is TdApi.UpdateConnectionState -> onUpdateConnectionState(tdApiUpdate)
            is TdApi.UpdateOption -> onUpdateOption(tdApiUpdate)
        }
    }

    private fun onUpdateConnectionState(
        tdApiUpdateConnectionState: TdApi.UpdateConnectionState
    ) {
        val connectionState = tdApiUpdateConnectionState.state
        connectionStateAppSendChannel.offer(connectionState)
        connectionStateEnterPhoneNumberSendChannel.offer(connectionState)
    }

    private fun onUpdateOption(
        updateOption: TdApi.UpdateOption
    ) {
        val optionValue = updateOption.value

        if (updateOption.name == "enabled_proxy_id") {
            when (optionValue) {
                is TdApi.OptionValueInteger -> enabledProxyIdSendChannel.offer(optionValue.value)
                is TdApi.OptionValueEmpty -> enabledProxyIdSendChannel.offer(null)
                else -> assertionFailure()
            }
        }
    }

}
