package com.anvipo.angram.layers.core.base.interfaces.recyclerView.row

import android.view.View
import com.anvipo.angram.layers.core.base.classes.recyclerView.row.RowViewHolder
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.ViewData

interface RowViewData : ViewData {

    fun <BR : BaseRow, RVH : RowViewHolder<BR>> createRowViewHolder(itemView: View): RVH

}