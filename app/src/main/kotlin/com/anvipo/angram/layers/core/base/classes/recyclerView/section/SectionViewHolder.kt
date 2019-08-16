package com.anvipo.angram.layers.core.base.classes.recyclerView.section

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.BaseSection

abstract class SectionViewHolder<BS : BaseSection>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(section: BS)

}