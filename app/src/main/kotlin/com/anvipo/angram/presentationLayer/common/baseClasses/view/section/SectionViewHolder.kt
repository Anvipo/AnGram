package com.anvipo.angram.presentationLayer.common.baseClasses.view.section

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anvipo.angram.presentationLayer.common.interfaces.view.section.BaseSection

abstract class SectionViewHolder<BS : BaseSection>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(section: BS)

}