package com.anvipo.angram.layers.presentation.common.interfaces.view.section

import android.view.View
import com.anvipo.angram.layers.presentation.common.baseClasses.section.SectionViewHolder
import com.anvipo.angram.layers.presentation.common.interfaces.view.ViewData

interface SectionViewData : ViewData {

    fun <BS : BaseSection, SVH : SectionViewHolder<BS>> createSectionViewHolder(itemView: View): SVH

}