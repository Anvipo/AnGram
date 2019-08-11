package com.anvipo.angram.layers.application.launchSystem

import android.app.Application
import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule
import com.anvipo.angram.layers.application.di.LaunchSystemModule
import com.anvipo.angram.layers.application.di.LaunchSystemModule.connectionStateAppSendChannelQualifier
import com.anvipo.angram.layers.application.di.LaunchSystemModule.enabledProxyIdSendChannelQualifier
import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule
import com.anvipo.angram.layers.application.types.ConnectionStateSendChannel
import com.anvipo.angram.layers.application.types.EnabledProxyIdSendChannel
import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.CoreHelpers.debugLog
import com.anvipo.angram.layers.core.CoroutineExceptionHandlerWithLogger
import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.collections.IMutableStack
import com.anvipo.angram.layers.core.collections.MutableStack
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType
import com.anvipo.angram.layers.data.di.GatewaysModule
import com.anvipo.angram.layers.global.GlobalHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.di.AuthorizationCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule.connectionStateEnterPhoneNumberSendChannelQualifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.error.*
import kotlin.coroutines.CoroutineContext

class App :
    Application(),
    HasLogger,
    CoroutineScope {

    companion object {
        private lateinit var INSTANCE: App
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override val className: String = this::class.java.name

    override fun <T> additionalLogging(logObj: T) {
        launch(coroutineContext + CoroutineExceptionHandlerWithLogger()) {
            val systemMessage: SystemMessage = when (logObj) {
                is String -> createTGSystemMessage(logObj)
                is SystemMessage -> logObj
                else -> TODO()
            }

            systemMessageSendChannel.send(systemMessage)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        INSTANCE = this
    }

    fun handleUpdates(tdApiObject: TdApi.Object) {
        log(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = tdApiObject.toString()
        )

        tdObjectsStack.push(tdApiObject)

        when (tdApiObject) {
            is TdApi.Update -> onUpdate(tdApiObject)
            else -> debugLog("Unhandled TdApi.Object case")
        }
    }

    fun handleUpdatesException(error: Throwable) {
        val text = error.localizedMessage

        val systemMessage: SystemMessage =
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
                    SystemMessage(
                        text = text,
                        type = SystemMessageType.ALERT,
                        shouldBeShownToUser = IS_IN_DEBUG_MODE,
                        shouldBeShownInLogs = false
                    )
                }
                else -> createTGSystemMessage(text)
            }

        val customLogging: () -> Unit = {
            additionalLogging(systemMessage)
        }

        log(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = text,
            customLogging = customLogging
        )

        tdErrorsStack.push(error)
    }

    fun handleDefaultException(error: Throwable) {
        log(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = error.localizedMessage
        )

        tdErrorsStack.push(error)
    }


    private fun initDI() {
        val debugModules = listOf(
            LaunchSystemModule.module,
            SystemInfrastructureModule.module,
            UseCasesModule.module,
            GatewaysModule.module,
            ApplicationCoordinatorModule.module,
            AuthorizationCoordinatorModule.module,
            EnterPhoneNumberModule.module,
            EnterAuthenticationCodeModule.module,
            EnterAuthenticationPasswordModule.module,
            AddProxyModule.module
        )

        if (IS_IN_DEBUG_MODE) {
            startKoin {
                androidLogger()

                androidContext(this@App)

                modules(debugModules)
            }
        } else {
            // TODO: release config
            debugLog("TODO")
        }

        log(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name
        )
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
