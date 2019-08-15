package com.anvipo.angram.layers.core.base.classes.section

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anvipo.angram.layers.core.base.interfaces.view.section.BaseSection

abstract class SectionViewHolder<BS : BaseSection>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(section: BS)

}