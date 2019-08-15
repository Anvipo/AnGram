package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.mtProto

import android.view.View
import com.anvipo.angram.R
import com.anvipo.angram.layers.presentation.common.baseClasses.row.RowViewHolder
import com.anvipo.angram.layers.presentation.common.interfaces.view.row.BaseRow
import com.anvipo.angram.layers.presentation.common.interfaces.view.row.RowViewData


object AddMTProtoProxyRowViewData : RowViewData {

    override val viewType: Int = 1
    override val layoutRes: Int = R.layout.item_add_proxy

    @Suppress("UNCHECKED_CAST")
    override fun <BR : BaseRow, RVH : RowViewHolder<BR>> createRowViewHolder(itemView: View): RVH =
        AddMTProtoProxyRowViewHolder(itemView) as RVH

}