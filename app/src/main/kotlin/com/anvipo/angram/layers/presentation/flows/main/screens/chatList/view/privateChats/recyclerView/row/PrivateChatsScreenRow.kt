package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.row

import com.anvipo.angram.layers.core.IndexPath
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.row.ChatListScreenRow

class PrivateChatsScreenRow(
    override val indexPath: IndexPath
) : ChatListScreenRow() {

    override val viewType: Int = PrivateChatsScreenRowViewData.viewType

}