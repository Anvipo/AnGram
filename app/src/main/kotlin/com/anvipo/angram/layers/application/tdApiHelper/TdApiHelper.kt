package com.anvipo.angram.layers.application.tdApiHelper

import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule.tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.tdApiUpdateConnectionStateAppViewModelSendChannelQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedSendChannel
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.basicGroupsFullInfoMutableMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.basicGroupsMutableMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.chatListQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.chatsMutableMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.enabledProxyIdSendChannelQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.secretChatsMutableMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.superGroupsMutableMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.supergroupsFullInfoMutableMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.tdLibClientHasBeenRecreatedSendChannelQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.usersFullInfoMutableMapQualifier
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.usersMutableMapQualifier
import com.anvipo.angram.layers.core.CoreHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.errorMessage
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType.ALERT
import com.anvipo.angram.layers.data.di.GatewaysModule.tdClientScopeQualifier
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.layers.global.OrderedChat
import com.anvipo.angram.layers.global.types.*
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.tdApiUpdateConnectionStateAuthorizationFlowSendChannelQualifier
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.KoinComponent
import org.koin.core.error.*
import org.koin.core.inject
import org.koin.core.scope.Scope
import kotlin.collections.set

object TdApiHelper : HasLogger, KoinComponent {

    override val className: String = this::class.java.name

    lateinit var tdClientScope: Scope

    var lastConnectionState: TdApi.ConnectionState? = null
        private set

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
            myLog(
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

    private val tdApiUpdateConnectionStateApplicationPresenterSendChannel
            by inject<TdApiUpdateConnectionStateSendChannel>(
                tdApiUpdateConnectionStateAppViewModelSendChannelQualifier
            )

    private val tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannel
            by inject<TdApiUpdateAuthorizationStateSendChannel>(
                tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier
            )

    private val tdLibClientHasBeenRecreatedSendChannel: TDLibClientHasBeenRecreatedSendChannel
            by inject(tdLibClientHasBeenRecreatedSendChannelQualifier)

    private val basicGroups: MutableMap<Int, TdApi.BasicGroup>
            by inject(basicGroupsMutableMapQualifier)
    private val superGroups: MutableMap<Int, TdApi.Supergroup>
            by inject(superGroupsMutableMapQualifier)
    private val secretChats: MutableMap<Int, TdApi.SecretChat>
            by inject(secretChatsMutableMapQualifier)
    private val chats: MutableMap<Long, TdApi.Chat> by inject(chatsMutableMapQualifier)

    private val users: MutableMap<Int, TdApi.User> by inject(usersMutableMapQualifier)
    private val chatList: MutableSet<OrderedChat> by inject(chatListQualifier)
    private val usersFullInfo: MutableMap<Int, TdApi.UserFullInfo>
            by inject(usersFullInfoMutableMapQualifier)
    private val basicGroupsFullInfo: MutableMap<Int, TdApi.BasicGroupFullInfo>
            by inject(basicGroupsFullInfoMutableMapQualifier)
    private val supergroupsFullInfo: MutableMap<Int, TdApi.SupergroupFullInfo>
            by inject(supergroupsFullInfoMutableMapQualifier)

    private fun onUpdate(tdApiUpdate: TdApi.Update) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

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
            is TdApi.UpdateChatTitle -> onUpdateChatTitle(tdApiUpdate)
            is TdApi.UpdateChatPhoto -> onUpdateChatPhoto(tdApiUpdate)
            is TdApi.UpdateChatLastMessage -> onUpdateChatLastMessage(tdApiUpdate)
            is TdApi.UpdateChatOrder -> onUpdateChatOrder(tdApiUpdate)
            is TdApi.UpdateChatIsPinned -> onUpdateChatIsPinned(tdApiUpdate)
            is TdApi.UpdateChatReadInbox -> onUpdateChatReadInbox(tdApiUpdate)
            is TdApi.UpdateChatReadOutbox -> onUpdateChatReadOutbox(tdApiUpdate)
            is TdApi.UpdateChatUnreadMentionCount -> onUpdateChatUnreadMentionCount(tdApiUpdate)
            is TdApi.UpdateMessageMentionRead -> onUpdateMessageMentionRead(tdApiUpdate)
            is TdApi.UpdateChatReplyMarkup -> onUpdateChatReplyMarkup(tdApiUpdate)
            is TdApi.UpdateChatDraftMessage -> onUpdateChatDraftMessage(tdApiUpdate)
            is TdApi.UpdateChatNotificationSettings -> onUpdateChatNotificationSettings(tdApiUpdate)
            is TdApi.UpdateChatDefaultDisableNotification -> onUpdateChatDefaultDisableNotification(
                tdApiUpdate
            )
            is TdApi.UpdateChatIsMarkedAsUnread -> onUpdateChatIsMarkedAsUnread(tdApiUpdate)
            is TdApi.UpdateChatIsSponsored -> onUpdateChatIsSponsored(tdApiUpdate)
            is TdApi.UpdateUserFullInfo -> onUpdateUserFullInfo(tdApiUpdate)
            is TdApi.UpdateBasicGroupFullInfo -> onUpdateBasicGroupFullInfo(tdApiUpdate)
            is TdApi.UpdateSupergroupFullInfo -> onUpdateSupergroupFullInfo(tdApiUpdate)
            else -> myLog(
                invokationPlace = invokationPlace,
                text = "Unhandled tdApiUpdate = $tdApiUpdate"
            )
        }
    }


    private fun onUpdateAuthorizationState(
        updateAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
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
                        myLog(
                            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                            text = "couldImmediatelySend = $couldImmediatelySend"
                        )
                    }
                }
                is TdApi.OptionValueEmpty -> {
                    val couldImmediatelySend = enabledProxyIdSendChannel.offer(null)

                    if (!couldImmediatelySend) {
                        myLog(
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
        val user = updateUser.user

        users[user.id] = user
    }

    private fun onUpdateUserStatus(updateUserStatus: TdApi.UpdateUserStatus) {
        val user = users[updateUserStatus.userId] ?: return

        synchronized(user) {
            user.status = updateUserStatus.status
        }
    }

    private fun onUpdateBasicGroup(updateBasicGroup: TdApi.UpdateBasicGroup) {
        val basicGroup = updateBasicGroup.basicGroup

        basicGroups[basicGroup.id] = basicGroup
    }

    private fun onUpdateSupergroup(updateSupergroup: TdApi.UpdateSupergroup) {
        val supergroup = updateSupergroup.supergroup

        superGroups[supergroup.id] = supergroup
    }

    private fun onUpdateSecretChat(updateSecretChat: TdApi.UpdateSecretChat) {
        val secretChat = updateSecretChat.secretChat

        secretChats[secretChat.id] = secretChat
    }

    private fun onUpdateNewChat(updateNewChat: TdApi.UpdateNewChat) {
        val chat = updateNewChat.chat

        synchronized(chat) {
            chats[chat.id] = chat

            val order = chat.order
            chat.order = 0
            setChatOrder(chat, order)
        }
    }

    private fun setChatOrder(
        chat: TdApi.Chat,
        order: Long
    ) {
        synchronized(chatList) {
            val chatOrder = chat.order
            val chatId = chat.id

            if (chatOrder != 0L) {
                val isRemoved = chatList.remove(OrderedChat(chatOrder, chatId))
                assert(isRemoved)
            }

            chat.order = order

            if (chatOrder != 0L) {
                val isAdded = chatList.add(OrderedChat(chatOrder, chatId))
                assert(isAdded)
            }
        }
    }

    private fun onUpdateChatTitle(updateChatTitle: TdApi.UpdateChatTitle) {
        val chat = chats[updateChatTitle.chatId] ?: return

        synchronized(chat) {
            chat.title = updateChatTitle.title
        }
    }

    private fun onUpdateChatPhoto(updateChatPhoto: TdApi.UpdateChatPhoto) {
        val chat = chats[updateChatPhoto.chatId] ?: return

        synchronized(chat) {
            chat.photo = updateChatPhoto.photo
        }
    }

    private fun onUpdateChatLastMessage(updateChatLastMessage: TdApi.UpdateChatLastMessage) {
        val chat = chats[updateChatLastMessage.chatId] ?: return

        synchronized(chat) {
            chat.lastMessage = updateChatLastMessage.lastMessage
            setChatOrder(chat, updateChatLastMessage.order)
        }
    }

    private fun onUpdateChatOrder(updateChatOrder: TdApi.UpdateChatOrder) {
        val chat = chats[updateChatOrder.chatId] ?: return

        synchronized(chat) {
            setChatOrder(chat, updateChatOrder.order)
        }
    }

    private fun onUpdateChatIsPinned(updateChatIsPinned: TdApi.UpdateChatIsPinned) {
        val chat = chats[updateChatIsPinned.chatId] ?: return

        synchronized(chat) {
            chat.isPinned = updateChatIsPinned.isPinned
            setChatOrder(chat, updateChatIsPinned.order)
        }
    }

    private fun onUpdateChatReadInbox(updateChatReadInbox: TdApi.UpdateChatReadInbox) {
        val chat = chats[updateChatReadInbox.chatId] ?: return

        synchronized(chat) {
            chat.lastReadInboxMessageId = updateChatReadInbox.lastReadInboxMessageId
            chat.unreadCount = updateChatReadInbox.unreadCount
        }
    }

    private fun onUpdateChatReadOutbox(updateChatReadOutbox: TdApi.UpdateChatReadOutbox) {
        val chat = chats[updateChatReadOutbox.chatId] ?: return

        synchronized(chat) {
            chat.lastReadOutboxMessageId = updateChatReadOutbox.lastReadOutboxMessageId
        }
    }

    private fun onUpdateChatUnreadMentionCount(
        updateChatUnreadMentionCount: TdApi.UpdateChatUnreadMentionCount
    ) {
        val chat = chats[updateChatUnreadMentionCount.chatId] ?: return

        synchronized(chat) {
            chat.unreadMentionCount = updateChatUnreadMentionCount.unreadMentionCount
        }
    }

    private fun onUpdateMessageMentionRead(
        updateMessageMentionRead: TdApi.UpdateMessageMentionRead
    ) {
        val chat = chats[updateMessageMentionRead.chatId] ?: return

        synchronized(chat) {
            chat.unreadMentionCount = updateMessageMentionRead.unreadMentionCount
        }
    }

    private fun onUpdateChatReplyMarkup(updateChatReplyMarkup: TdApi.UpdateChatReplyMarkup) {
        val chat = chats[updateChatReplyMarkup.chatId] ?: return

        synchronized(chat) {
            chat.replyMarkupMessageId = updateChatReplyMarkup.replyMarkupMessageId
        }
    }

    private fun onUpdateChatDraftMessage(
        updateChatDraftMessage: TdApi.UpdateChatDraftMessage
    ) {
        val chat = chats[updateChatDraftMessage.chatId] ?: return

        synchronized(chat) {
            chat.draftMessage = updateChatDraftMessage.draftMessage
            setChatOrder(chat, updateChatDraftMessage.order)
        }
    }

    private fun onUpdateChatNotificationSettings(
        updateChatNotificationSettings: TdApi.UpdateChatNotificationSettings
    ) {
        val chat = chats[updateChatNotificationSettings.chatId] ?: return

        synchronized(chat) {
            chat.notificationSettings = updateChatNotificationSettings.notificationSettings
        }
    }

    private fun onUpdateChatDefaultDisableNotification(
        updateChatDefaultDisableNotification: TdApi.UpdateChatDefaultDisableNotification
    ) {
        val chat = chats[updateChatDefaultDisableNotification.chatId] ?: return

        synchronized(chat) {
            chat.defaultDisableNotification =
                updateChatDefaultDisableNotification.defaultDisableNotification
        }
    }

    private fun onUpdateChatIsMarkedAsUnread(
        updateChatIsMarkedAsUnread: TdApi.UpdateChatIsMarkedAsUnread
    ) {
        val chat = chats[updateChatIsMarkedAsUnread.chatId] ?: return

        synchronized(chat) {
            chat.isMarkedAsUnread = updateChatIsMarkedAsUnread.isMarkedAsUnread
        }
    }

    private fun onUpdateChatIsSponsored(updateChatIsSponsored: TdApi.UpdateChatIsSponsored) {
        val chat = chats[updateChatIsSponsored.chatId] ?: return

        synchronized(chat) {
            chat.isSponsored = updateChatIsSponsored.isSponsored
            setChatOrder(chat, updateChatIsSponsored.order)
        }
    }

    private fun onUpdateUserFullInfo(updateUserFullInfo: TdApi.UpdateUserFullInfo) {
        usersFullInfo[updateUserFullInfo.userId] = updateUserFullInfo.userFullInfo
    }

    private fun onUpdateBasicGroupFullInfo(
        updateBasicGroupFullInfo: TdApi.UpdateBasicGroupFullInfo
    ) {
        basicGroupsFullInfo[updateBasicGroupFullInfo.basicGroupId] =
            updateBasicGroupFullInfo.basicGroupFullInfo
    }

    private fun onUpdateSupergroupFullInfo(
        updateSupergroupFullInfo: TdApi.UpdateSupergroupFullInfo
    ) {
        supergroupsFullInfo[updateSupergroupFullInfo.supergroupId] =
            updateSupergroupFullInfo.supergroupFullInfo
    }


    private fun brodcastNewTdApiConnectionState(
        updateConnectionState: TdApi.UpdateConnectionState
    ) {
        synchronized(updateConnectionState) {
            lastConnectionState = updateConnectionState.state

            tdApiUpdateConnectionStateApplicationPresenterSendChannel.also {
                val couldImmediatelySend = it.offer(updateConnectionState)

                if (!couldImmediatelySend) {
                    myLog(
                        invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                        text = "couldImmediatelySend = $couldImmediatelySend"
                    )
                }
            }

            authorizationCoordinatorScope?.apply {
                get<TdApiUpdateConnectionStateSendChannel>(
                    tdApiUpdateConnectionStateAuthorizationFlowSendChannelQualifier
                ).also {
                    val couldImmediatelySend = it.offer(updateConnectionState)

                    if (!couldImmediatelySend) {
                        myLog(
                            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                            text = "couldImmediatelySend = $couldImmediatelySend"
                        )
                    }
                }
            }
        }
    }

    private fun brodcastNewTdApiAuthorizationState(
        updateAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        synchronized(updateAuthorizationState) {
            tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannel.also {
                val couldImmediatelySend =
                    it.offer(
                        updateAuthorizationState
                    )

                if (!couldImmediatelySend) {
                    myLog(
                        invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                        text = "couldImmediatelySend = $couldImmediatelySend"
                    )
                }
            }

            authorizationCoordinatorScope?.apply {
                get<TdApiUpdateAuthorizationStateSendChannel>(
                    tdApiUpdateAuthorizationStateAuthorizationCoordinatorSendChannelQualifier
                ).also {
                    val couldImmediatelySend = it.offer(updateAuthorizationState)

                    if (!couldImmediatelySend) {
                        myLog(
                            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                            text = "couldImmediatelySend = $couldImmediatelySend"
                        )
                    }
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