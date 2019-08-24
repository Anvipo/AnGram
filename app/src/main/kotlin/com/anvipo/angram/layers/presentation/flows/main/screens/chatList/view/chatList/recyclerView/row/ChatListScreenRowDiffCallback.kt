package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.row

import com.anvipo.angram.layers.core.base.classes.recyclerView.row.BaseRowDiffCallback

abstract class ChatListScreenRowDiffCallback<CLSR : ChatListScreenRow> : BaseRowDiffCallback<CLSR>() {

    final override fun areContentsTheSame(
        oldItem: CLSR,
        newItem: CLSR
    ): Boolean = TODO()

}