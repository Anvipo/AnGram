package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.row

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.anvipo.angram.layers.core.base.classes.recyclerView.row.RowListAdapter

@Suppress("unused")
abstract class ChatListScreenRowListAdapter<
        CLSR : ChatListScreenRow,
        RVH : ChatListScreenRowViewHolder<CLSR>
        > : RowListAdapter<CLSR, RVH> {

    constructor(
        diffCallback: DiffUtil.ItemCallback<CLSR>
    ) : super(diffCallback)

    constructor(
        config: AsyncDifferConfig<CLSR>
    ) : super(config)

}