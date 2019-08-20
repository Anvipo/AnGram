package com.anvipo.angram.layers.application.tdApiHelper

import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule.tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.tdApiUpdateConnectionStateAppViewModelSendChannelQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedSendChannel
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.basicGroupsMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.chatsMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.enabledProxyIdSendChannelQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.secretChatsMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.superGroupsMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.tdLibClientHasBeenRecreatedSendChannelQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.usersMapQualifier
import com.anvipo.angram.layers.core.CoreHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType.ALERT
import com.anvipo.angram.layers.data.di.GatewaysModule.tdClientScopeQualifier
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.layers.global.types.*
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule.tdApiUpdateConnectionStateEnterAuthenticationPhoneNumberScreenSendChannelQualifier
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.KoinComponent
import org.koin.core.error.*
import org.koin.core.get
import org.koin.core.inject
import org.koin.core.scope.Scope
import java.util.concurrent.ConcurrentMap
import kotlin.collections.set

object TdApiHelper : HasLogger, KoinComponent {

    override val className: String = this::class.java.name

    lateinit var tdClientScope: Scope

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

    fun onCompletedDISetup() {
        tdClientScope = getKoin().createScope(
            scopeId = tdClientScopeID,
            qualifier = tdClientScopeQualifier
        )
    }

    fun handleTDLibUpdate(tdApiObject: TdApi.Object) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        val text = "tdApiObject = $tdApiObject"
        myLog(
            invokationPlace = invokationPlace,
            text = text
        )

        when (tdApiObject) {
            is TdApi.Update -> onUpdate(tdApiObject)
            else -> myLog(
                text = text,
                invokationPlace = invokationPlace
            )
        }
    }

    fun handleTDLibUpdatesException(tdLibUpdatesException: TDLibUpdatesException) {
        val text = "tdLibUpdatesException.errorMessage = ${tdLibUpdatesException.errorMessage}"

        val systemMessage = createSystemMessageFrom(
            tdLibUpdatesException,
            text
        )

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

    private const val tdClientScopeID = "TD client scope ID"

    private val systemMessageSendChannel: SystemMessageSendChannel
            by inject(systemMessageSendChannelQualifier)

    private val enabledProxyIdSendChannel: EnabledProxyIdSendChannel
            by inject(enabledProxyIdSendChannelQualifier)

    private val tdApiUpdateConnectionStateSendChanels by lazy {
        val tdApiUpdateConnectionStateApplicationPresenterSendChannel =
            get<TdApiUpdateConnectionStateSendChannel>(
                tdApiUpdateConnectionStateAppViewModelSendChannelQualifier
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

    private val tdLibClientHasBeenRecreatedSendChannel: TDLibClientHasBeenRecreatedSendChannel
            by inject(tdLibClientHasBeenRecreatedSendChannelQualifier)

    private val users: ConcurrentMap<Int, TdApi.User> by inject(usersMapQualifier)
    private val basicGroups: ConcurrentMap<Int, TdApi.BasicGroup> by inject(basicGroupsMapQualifier)
    private val superGroups: ConcurrentMap<Int, TdApi.Supergroup> by inject(superGroupsMapQualifier)
    private val secretChats: ConcurrentMap<Int, TdApi.SecretChat> by inject(secretChatsMapQualifier)
    private val chats: ConcurrentMap<Long, TdApi.Chat> by inject(chatsMapQualifier)

    private fun onUpdate(tdApiUpdate: TdApi.Update) {
        when (tdApiUpdate) {
            is TdApi.UpdateAuthorizationState -> onUpdateAuthorizationState(tdApiUpdate)
            is TdApi.UpdateConnectionState -> onUpdateConnectionState(tdApiUpdate)
            is TdApi.UpdateOption -> onUpdateOption(tdApiUpdate)
            is TdApi.UpdateUser -> onUpdateUser(tdApiUpdate)
            is TdApi.UpdateUserStatus -> onUpdateUserStatus(tdApiUpdate)
            is TdApi.UpdateBasicGroup -> onUpdateBasicGroup(tdApiUpdate)
            is TdApi.UpdateSupergroup -> onUpdateSupergroup(tdApiUpdate)
            is TdApi.UpdateSecretChat -> onUpdateSecretChat(tdApiUpdate)
            is TdApi.UpdateNewChat -> onUpdateNewChat(tdApiUpdate)
            else -> {
                println()
            }
        }
    }

    private fun onUpdateAuthorizationState(updateAuthorizationState: TdApi.UpdateAuthorizationState) {
        brodcastNewTdApiAuthorizationState(updateAuthorizationState)
    }

    private fun onUpdateConnectionState(updateConnectionState: TdApi.UpdateConnectionState) {
        brodcastNewTdApiConnectionState(updateConnectionState)
    }

    private fun onUpdateOption(updateOption: TdApi.UpdateOption) {
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

    private fun onUpdateUser(updateUser: TdApi.UpdateUser) {
        users[updateUser.user.id] = updateUser.user
    }

    private fun onUpdateUserStatus(updateUserStatus: TdApi.UpdateUserStatus) {
        val user = users[updateUserStatus.userId] ?: return

        synchronized(user) {
            user.status = updateUserStatus.status
        }
    }

    private fun onUpdateBasicGroup(updateBasicGroup: TdApi.UpdateBasicGroup) {
        basicGroups[updateBasicGroup.basicGroup.id] = updateBasicGroup.basicGroup
    }

    private fun onUpdateSupergroup(updateSupergroup: TdApi.UpdateSupergroup) {
        superGroups[updateSupergroup.supergroup.id] = updateSupergroup.supergroup
    }

    private fun onUpdateSecretChat(updateSecretChat: TdApi.UpdateSecretChat) {
        secretChats[updateSecretChat.secretChat.id] = updateSecretChat.secretChat
    }

    private fun onUpdateNewChat(updateNewChat: TdApi.UpdateNewChat) {
        val chat = updateNewChat.chat
        synchronized(chat) {
            chats[chat.id] = chat

            val order = chat.order
            chat.order = 0
//            setChatOrder(chat, order)
        }
    }


    private fun brodcastNewTdApiConnectionState(updateConnectionState: TdApi.UpdateConnectionState) {
        tdApiUpdateConnectionStateSendChanels.forEach {
            val couldImmediatelySend = it.offer(updateConnectionState)

            if (!couldImmediatelySend) {
                logIfShould(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    text = "couldImmediatelySend = $couldImmediatelySend"
                )
            }
        }
    }

    private fun brodcastNewTdApiAuthorizationState(updateAuthorizationState: TdApi.UpdateAuthorizationState) {
        tdApiUpdateAuthorizationStateSendChannels.forEach {
            val couldImmediatelySend = it.offer(updateAuthorizationState)

            if (!couldImmediatelySend) {
                logIfShould(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    text = "couldImmediatelySend = $couldImmediatelySend"
                )
            }
        }

        if (updateAuthorizationState.authorizationState is TdApi.AuthorizationStateClosed) {
            tdClientScope.close()
            tdClientScope = this.getKoin().createScope(
                scopeId = tdClientScopeID,
                qualifier = tdClientScopeQualifier
            )
            tdLibClientHasBeenRecreatedSendChannel.offer(Unit)
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
                type = ALERT,
                shouldBeShownToUser = IS_IN_DEBUG_MODE,
                shouldBeShownInLogs = false
            )
        }
        else -> createTGSystemMessage(text)
    }

}