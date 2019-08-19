package com.anvipo.angram.layers.application.launchSystem

import android.app.Application
import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule
import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule.tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.enabledProxyIdSendChannelQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.tdApiUpdateConnectionStateAppPresenterSendChannelQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule
import com.anvipo.angram.layers.core.CoreHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import com.anvipo.angram.layers.core.UiScope
import com.anvipo.angram.layers.core.base.interfaces.HasMyCoroutineBuilders
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType
import com.anvipo.angram.layers.data.di.GatewaysModule
import com.anvipo.angram.layers.data.di.GatewaysModule.mustRecreateTDLibClientSendChannelQualifier
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.layers.global.types.*
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule.tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.error.*

class App :
    Application(),
    HasLogger,
    HasMyCoroutineBuilders,
    CoroutineScope by UiScope() {

    override val className: String by lazy { this::class.java.name }

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    override fun onTerminate() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        try {
            cancel(cancellationException)
        } catch (exception: Exception) {
            myLog(
                invokationPlace = methodName,
                text = "exception = $exception"
            )
        }

        GatewaysModule.stopCoroutinesWork()
        super.onTerminate()
    }


    override fun <T : Any> additionalLogging(logObj: T) {
        val systemMessage: SystemMessage = when (logObj) {
            is String -> createTGSystemMessage(logObj)
            is SystemMessage -> logObj
            is Unit -> return
            else -> {
                val message = "Undefined logObj type = $logObj"
                assertionFailure(message)
                TODO(message)
            }
        }

        val couldImmediatelySend = systemMessageSendChannel.offer(systemMessage)

        if (!couldImmediatelySend) {
            logIfShould(
                invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                text = "couldImmediatelySend = $couldImmediatelySend"
            )
        }
    }

    override fun myLaunchExceptionHandler(throwable: Throwable) {
        additionalLogging(throwable.errorMessage)
    }


    fun handleTDLibUpdate(tdApiObject: TdApiObject) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        val text = "tdApiObject = $tdApiObject"
        myLog(
            invokationPlace = invokationPlace,
            text = text
        )

        when (tdApiObject) {
            is TdApiUpdate -> onUpdate(tdApiObject)
            else -> myLog(
                text = text,
                invokationPlace = invokationPlace
            )
        }
    }

    fun handleTDLibUpdatesException(tdLibUpdatesException: TDLibUpdatesException) {
        val text = "tdLibUpdatesException.errorMessage = ${tdLibUpdatesException.errorMessage}"

        val systemMessage = createSystemMessageFrom(tdLibUpdatesException, text)

        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "tdLibUpdatesException.errorMessage = ${tdLibUpdatesException.errorMessage}",
            customLogging = { additionalLogging(systemMessage) }
        )
    }

    fun handleTDLibDefaultException(tdLibDefaultException: TDLibDefaultException) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "tdLibDefaultException.errorMessage = ${tdLibDefaultException.errorMessage}"
        )
    }


    private val systemMessageSendChannel: SystemMessageSendChannel
            by inject(systemMessageSendChannelQualifier)

    private val enabledProxyIdSendChannel: EnabledProxyIdSendChannel
            by inject(enabledProxyIdSendChannelQualifier)

    private val tdApiUpdateConnectionStateSendChanels by lazy {
        val tdApiUpdateConnectionStateApplicationPresenterSendChannel =
            get<TdApiUpdateConnectionStateSendChannel>(
                tdApiUpdateConnectionStateAppPresenterSendChannelQualifier
            )

        val tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannel =
            get<TdApiUpdateConnectionStateSendChannel>(
                tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier
            )

        listOf(
            tdApiUpdateConnectionStateApplicationPresenterSendChannel,
            tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannel
        )
    }

    private val tdApiUpdateAuthorizationStateSendChannels by lazy {
        val tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannel =
            get<TdApiUpdateAuthorizationStateSendChannel>(
                tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier
            )

        val tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannel =
            get<TdApiUpdateAuthorizationStateSendChannel>(
                tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
            )

        listOf(
            tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannel,
            tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannel
        )
    }

    private val mustRecreateTDLibClientSendChannel by inject<MustRecreateTDLibClientSendChannel>(
        mustRecreateTDLibClientSendChannelQualifier
    )

    @ExperimentalCoroutinesApi
    private fun initDI() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        val debugModules by lazy {
            listOf(
                SystemInfrastructureModule.module,
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
        }

        if (IS_IN_DEBUG_MODE) {
            startKoin {
                androidLogger()

                androidContext(this@App)

                modules(debugModules)
            }
        } else {
            // TODO: release config
            myLog(
                invokationPlace = invokationPlace,
                text = "TODO"
            )
        }

        myLog(invokationPlace = invokationPlace)
    }


    private fun onUpdate(
        tdApiUpdate: TdApiUpdate
    ) {
        when (tdApiUpdate) {
            is TdApiUpdateAuthorizationState -> onUpdateAuthorizationState(tdApiUpdate)
            is TdApiUpdateConnectionState -> onUpdateConnectionState(tdApiUpdate)
            is TdApiUpdateOption -> onUpdateOption(tdApiUpdate)
        }
    }

    private fun onUpdateAuthorizationState(updateAuthorizationState: TdApiUpdateAuthorizationState) {
        brodcastNewTdApiAuthorizationState(updateAuthorizationState)
    }

    private fun onUpdateConnectionState(
        tdApiUpdateConnectionState: TdApiUpdateConnectionState
    ) {
        brodcastNewTdApiConnectionState(tdApiUpdateConnectionState)
    }

    private fun onUpdateOption(
        updateOption: TdApiUpdateOption
    ) {
        val optionValue = updateOption.value

        if (updateOption.name == "enabled_proxy_id") {
            when (optionValue) {
                is TdApi.OptionValueInteger -> {
                    val couldImmediatelySend = enabledProxyIdSendChannel.offer(optionValue.value)

                    if (!couldImmediatelySend) {
                        logIfShould(
                            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                            text = "couldImmediatelySend = $couldImmediatelySend"
                        )
                    }
                }
                is TdApi.OptionValueEmpty -> {
                    val couldImmediatelySend = enabledProxyIdSendChannel.offer(null)

                    if (!couldImmediatelySend) {
                        logIfShould(
                            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                            text = "couldImmediatelySend = $couldImmediatelySend"
                        )
                    }
                }
                else -> assertionFailure("Undefined enabled_proxy_id value = $optionValue")
            }
        }
    }


    private fun brodcastNewTdApiConnectionState(tdApiConnectionState: TdApiUpdateConnectionState) {
        tdApiUpdateConnectionStateSendChanels.forEach {
            val couldImmediatelySend = it.offer(tdApiConnectionState)

            if (!couldImmediatelySend) {
                logIfShould(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    text = "couldImmediatelySend = $couldImmediatelySend"
                )
            }
        }
    }

    private fun brodcastNewTdApiAuthorizationState(
        tdApiAuthorizationState: TdApiUpdateAuthorizationState
    ) {
        tdApiUpdateAuthorizationStateSendChannels.forEach {
            val couldImmediatelySend = it.offer(tdApiAuthorizationState)

            if (!couldImmediatelySend) {
                logIfShould(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    text = "couldImmediatelySend = $couldImmediatelySend"
                )
            }
        }

        if (tdApiAuthorizationState.authorizationState is TdApi.AuthorizationStateClosed) {
            val couldImmediatelySend = mustRecreateTDLibClientSendChannel.offer(MustRecreateTDLibClient)

            if (!couldImmediatelySend) {
                logIfShould(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    text = "couldImmediatelySend = $couldImmediatelySend"
                )
            }
        }
    }


    private fun createSystemMessageFrom(
        error: Throwable,
        text: String
    ): SystemMessage = when (error) {
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

}
