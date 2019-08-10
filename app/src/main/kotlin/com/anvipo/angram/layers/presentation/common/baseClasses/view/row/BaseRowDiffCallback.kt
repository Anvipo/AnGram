package com.anvipo.angram.layers.presentation.common.baseClasses.view.row

import androidx.recyclerview.widget.DiffUtil
import com.anvipo.angram.layers.presentation.common.interfaces.view.row.BaseRow

abstract class BaseRowDiffCallback<BR : BaseRow> :
    DiffUtil.ItemCallback<BR>() {

    @Suppress("EXPERIMENTAL_API_USAGE")
    final override fun areItemsTheSame(
        oldItem: BR,
        newItem: BR
    ): Boolean = oldItem.indexPath == newItem.indexPath

}