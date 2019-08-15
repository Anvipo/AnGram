package com.anvipo.angram.layers.core.base.interfaces.view.row

import android.view.View
import com.anvipo.angram.layers.core.base.classes.row.RowViewHolder
import com.anvipo.angram.layers.core.base.interfaces.view.ViewData

interface RowViewData : ViewData {

    fun <BR : BaseRow, RVH : RowViewHolder<BR>> createRowViewHolder(itemView: View): RVH

}