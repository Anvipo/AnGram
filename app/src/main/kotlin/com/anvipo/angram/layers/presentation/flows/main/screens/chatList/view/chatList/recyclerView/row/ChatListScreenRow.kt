package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.row

import com.anvipo.angram.layers.core.base.interfaces.recyclerView.row.BaseRow
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.row.RowViewData
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.row.PrivateChatsScreenRowViewData

abstract class ChatListScreenRow : BaseRow {

    final override val viewsData: List<RowViewData> =
        listOf(
            PrivateChatsScreenRowViewData
        )

}