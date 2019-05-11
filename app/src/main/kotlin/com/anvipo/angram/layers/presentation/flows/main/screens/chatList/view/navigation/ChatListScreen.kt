package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.navigation

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.ChatListFragment
import org.koin.core.KoinComponent
import org.koin.core.get
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ChatListScreen : SupportAppScreen(), KoinComponent {

    override fun getFragment(): Fragment = get<ChatListFragment>()

}