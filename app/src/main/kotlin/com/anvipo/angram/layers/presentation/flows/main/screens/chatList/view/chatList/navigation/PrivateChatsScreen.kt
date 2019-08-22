package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.navigation

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.PrivateChatsFragment
import org.koin.core.KoinComponent
import org.koin.core.get
import ru.terrakok.cicerone.android.support.SupportAppScreen

object PrivateChatsScreen : SupportAppScreen(), KoinComponent {

    override fun getFragment(): Fragment = get<PrivateChatsFragment>()

}