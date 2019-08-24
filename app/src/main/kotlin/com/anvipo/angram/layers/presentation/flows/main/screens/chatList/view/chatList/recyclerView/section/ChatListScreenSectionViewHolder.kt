package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.section

import android.view.View
import com.anvipo.angram.layers.core.base.classes.recyclerView.section.SectionViewHolder

abstract class ChatListScreenSectionViewHolder<CLSS : ChatListScreenSection>(
    itemView: View
) : SectionViewHolder<CLSS>(itemView)