package com.anvipo.angram.layers.core.base.classes.recyclerView.section

import androidx.recyclerview.widget.DiffUtil
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.BaseSection

abstract class BaseSectionDiffCallback<BS : BaseSection> : DiffUtil.ItemCallback<BS>() {

    @ExperimentalUnsignedTypes
    final override fun areItemsTheSame(
        oldItem: BS,
        newItem: BS
    ): Boolean = oldItem.index == newItem.index

    final override fun areContentsTheSame(
        oldItem: BS,
        newItem: BS
    ): Boolean = oldItem.items == newItem.items

}