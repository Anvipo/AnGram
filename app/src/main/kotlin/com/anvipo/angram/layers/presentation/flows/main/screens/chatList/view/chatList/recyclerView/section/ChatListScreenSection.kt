package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.section

import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.BaseSection
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.SectionViewData
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.section.PrivateChatsScreenSectionViewData

abstract class ChatListScreenSection : BaseSection {

    final override val viewsData: List<SectionViewData> =
        listOf(
            PrivateChatsScreenSectionViewData
        )

}