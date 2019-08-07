package com.anvipo.angram.presentationLayer.common.interfaces.view.row

import android.view.View
import com.anvipo.angram.presentationLayer.common.baseClasses.view.row.RowViewHolder
import com.anvipo.angram.presentationLayer.common.interfaces.view.ViewData

interface RowViewData : ViewData {

    fun <BR : BaseRow, RVH : RowViewHolder<BR>> createRowViewHolder(itemView: View): RVH

}