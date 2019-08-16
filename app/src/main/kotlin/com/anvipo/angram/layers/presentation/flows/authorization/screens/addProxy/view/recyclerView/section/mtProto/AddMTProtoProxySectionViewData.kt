package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.section.mtProto

import android.view.View
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.recyclerView.section.SectionViewHolder
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.BaseSection
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.SectionViewData

object AddMTProtoProxySectionViewData :
    SectionViewData {

    override val viewType: Int = 0
    override val layoutRes: Int = R.layout.section_add_proxy

    @Suppress("UNCHECKED_CAST")
    override fun <BS : BaseSection, SVH : SectionViewHolder<BS>> createSectionViewHolder(itemView: View): SVH =
        AddMTProtoProxySectionViewHolder(itemView) as SVH

}