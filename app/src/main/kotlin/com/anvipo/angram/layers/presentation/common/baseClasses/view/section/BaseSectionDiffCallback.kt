package com.anvipo.angram.layers.presentation.common.baseClasses.view.section

import androidx.recyclerview.widget.DiffUtil
import com.anvipo.angram.layers.presentation.common.interfaces.view.section.BaseSection

abstract class BaseSectionDiffCallback<BS : BaseSection> :
    DiffUtil.ItemCallback<BS>() {

    @Suppress("EXPERIMENTAL_API_USAGE")
    final override fun areItemsTheSame(
        oldItem: BS,
        newItem: BS
    ): Boolean = oldItem.index == newItem.index

    final override fun areContentsTheSame(
        oldItem: BS,
        newItem: BS
    ): Boolean = oldItem.items == newItem.items

}