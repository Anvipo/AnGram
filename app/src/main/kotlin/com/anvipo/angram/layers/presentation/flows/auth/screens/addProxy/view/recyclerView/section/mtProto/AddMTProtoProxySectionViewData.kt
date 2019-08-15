package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section.mtProto

import android.view.View
import com.anvipo.angram.R
import com.anvipo.angram.layers.presentation.common.baseClasses.section.SectionViewHolder
import com.anvipo.angram.layers.presentation.common.interfaces.view.section.BaseSection
import com.anvipo.angram.layers.presentation.common.interfaces.view.section.SectionViewData

object AddMTProtoProxySectionViewData :
    SectionViewData {

    override val viewType: Int = 0
    override val layoutRes: Int = R.layout.section_add_proxy

    @Suppress("UNCHECKED_CAST")
    override fun <BS : BaseSection, SVH : SectionViewHolder<BS>> createSectionViewHolder(itemView: View): SVH =
        AddMTProtoProxySectionViewHolder(itemView) as SVH

}