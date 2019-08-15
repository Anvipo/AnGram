package com.anvipo.angram.layers.core.base.classes.row

import androidx.recyclerview.widget.DiffUtil
import com.anvipo.angram.layers.core.base.interfaces.view.row.BaseRow

abstract class BaseRowDiffCallback<BR : BaseRow> :
    DiffUtil.ItemCallback<BR>() {

    final override fun areItemsTheSame(
        oldItem: BR,
        newItem: BR
    ): Boolean = oldItem.indexPath == newItem.indexPath

}