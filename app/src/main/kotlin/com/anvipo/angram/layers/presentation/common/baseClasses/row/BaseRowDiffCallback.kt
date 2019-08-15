package com.anvipo.angram.layers.presentation.common.baseClasses.row

import androidx.recyclerview.widget.DiffUtil
import com.anvipo.angram.layers.presentation.common.interfaces.view.row.BaseRow

abstract class BaseRowDiffCallback<BR : BaseRow> :
    DiffUtil.ItemCallback<BR>() {

    final override fun areItemsTheSame(
        oldItem: BR,
        newItem: BR
    ): Boolean = oldItem.indexPath == newItem.indexPath

}