package com.anvipo.angram.layers.core.base.interfaces.recyclerView.section

import android.view.View
import com.anvipo.angram.layers.core.base.classes.recyclerView.section.SectionViewHolder
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.ViewData

interface SectionViewData : ViewData {

    fun <BS : BaseSection, SVH : SectionViewHolder<BS>> createSectionViewHolder(itemView: View): SVH

}