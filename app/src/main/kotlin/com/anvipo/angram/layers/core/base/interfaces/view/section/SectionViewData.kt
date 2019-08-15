package com.anvipo.angram.layers.core.base.interfaces.view.section

import android.view.View
import com.anvipo.angram.layers.core.base.classes.section.SectionViewHolder
import com.anvipo.angram.layers.core.base.interfaces.view.ViewData

interface SectionViewData : ViewData {

    fun <BS : BaseSection, SVH : SectionViewHolder<BS>> createSectionViewHolder(itemView: View): SVH

}