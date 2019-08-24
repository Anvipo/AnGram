package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.section

import com.anvipo.angram.layers.core.base.interfaces.recyclerView.BaseHeaderFooterData
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.section.ChatListScreenSection
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.row.PrivateChatsScreenRow

class PrivateChatsScreenSection @ExperimentalUnsignedTypes constructor(
    override val viewType: Int = PrivateChatsScreenSectionViewData.viewType,
    override val index: UInt,
    override val items: List<PrivateChatsScreenRow>,
    override val headerData: BaseHeaderFooterData? = null,
    override val footerData: BaseHeaderFooterData? = null
) : ChatListScreenSection()