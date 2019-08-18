package com.anvipo.angram.layers.core.base.classes.recyclerView.section

import android.view.View
import com.anvipo.angram.layers.core.base.classes.recyclerView.LifecycleViewHolder
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.BaseSection

abstract class SectionViewHolder<BS : BaseSection>(itemView: View) :
    LifecycleViewHolder(itemView) {

    abstract fun bind(section: BS)

}