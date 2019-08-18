package com.anvipo.angram.layers.core.base.classes.recyclerView.row

import android.view.View
import com.anvipo.angram.layers.core.base.classes.recyclerView.LifecycleViewHolder
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.row.BaseRow

abstract class RowViewHolder<BR : BaseRow>(
    itemView: View
) : LifecycleViewHolder(itemView) {

    abstract fun bind(row: BR)

}