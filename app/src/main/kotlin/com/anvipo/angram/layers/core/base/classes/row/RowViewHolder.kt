package com.anvipo.angram.layers.core.base.classes.row

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anvipo.angram.layers.core.base.interfaces.view.row.BaseRow

abstract class RowViewHolder<BR : BaseRow>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(row: BR)

}