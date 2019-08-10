package com.anvipo.angram.presentationLayer.common.interfaces.view.section

import android.view.View
import com.anvipo.angram.presentationLayer.common.baseClasses.view.section.SectionViewHolder
import com.anvipo.angram.presentationLayer.common.interfaces.view.ViewData

interface SectionViewData : ViewData {

    fun <BS : BaseSection, SVH : SectionViewHolder<BS>> createSectionViewHolder(itemView: View): SVH

}