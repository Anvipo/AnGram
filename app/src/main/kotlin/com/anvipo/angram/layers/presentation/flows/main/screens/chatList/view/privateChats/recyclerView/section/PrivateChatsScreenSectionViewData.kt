package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.recyclerView.section

import android.view.View
import com.anvipo.angram.layers.core.base.classes.recyclerView.section.SectionViewHolder
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.BaseSection
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.SectionViewData

object PrivateChatsScreenSectionViewData : SectionViewData {

    @Suppress("UNCHECKED_CAST")
    override fun <BS : BaseSection, SVH : SectionViewHolder<BS>> createSectionViewHolder(itemView: View): SVH =
        PrivateChatsScreenViewHolder(itemView) as SVH

    override val viewType: Int = TODO("not implemented")
    override val layoutRes: Int = TODO("not implemented")

}