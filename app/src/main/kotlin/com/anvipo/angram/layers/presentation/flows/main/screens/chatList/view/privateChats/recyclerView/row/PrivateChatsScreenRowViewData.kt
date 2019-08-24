package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.row

import android.view.View
import com.anvipo.angram.layers.core.base.classes.recyclerView.row.RowViewHolder
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.row.BaseRow
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.row.RowViewData

object PrivateChatsScreenRowViewData : RowViewData {

    override val viewType: Int = 0
    override val layoutRes: Int = TODO()

    @Suppress("UNCHECKED_CAST")
    override fun <BR : BaseRow, RVH : RowViewHolder<BR>> createRowViewHolder(itemView: View): RVH =
        PrivateChatsScreenRowViewHolder(itemView) as RVH

}