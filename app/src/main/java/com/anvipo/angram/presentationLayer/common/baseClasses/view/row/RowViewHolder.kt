package com.anvipo.angram.presentationLayer.common.baseClasses.view.row

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anvipo.angram.presentationLayer.common.interfaces.view.row.BaseRow

abstract class RowViewHolder<BR : BaseRow>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(row: BR)

}