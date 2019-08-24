package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.section

import com.anvipo.angram.layers.core.base.classes.recyclerView.section.SectionListAdapter

class ChatListScreenSectionListAdapter : SectionListAdapter<
        ChatListScreenSection,
        ChatListScreenSectionViewHolder<ChatListScreenSection>
        >(ChatListScreenSectionDiffCallback())