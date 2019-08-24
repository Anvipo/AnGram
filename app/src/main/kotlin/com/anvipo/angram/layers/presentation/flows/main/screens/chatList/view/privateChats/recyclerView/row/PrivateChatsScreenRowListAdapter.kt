package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.row

import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.row.ChatListScreenRowListAdapter

class PrivateChatsScreenRowListAdapter :
    ChatListScreenRowListAdapter<
            PrivateChatsScreenRow,
            PrivateChatsScreenRowViewHolder
            >(PrivateChatsScreenRowDiffCallback())