package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.ChatListViewModel
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.ChatListViewModelFactory
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.ChatListViewModelImpl
import kotlinx.android.synthetic.main.fragment_chat_list.*
import org.koin.android.ext.android.get

class ChatListFragment : BaseFragment() {

    companion object {
        fun createNewInstance(): ChatListFragment = ChatListFragment()
    }

    override val viewModel: ChatListViewModel by viewModels<ChatListViewModelImpl> {
        get<ChatListViewModelFactory>()
    }
    override val actionBarTitle: String by lazy {
        getString(R.string.chat_list_action_bar_title)
    }
    override val actionBar: Toolbar
        get() = chat_list_toolbar as Toolbar
    override val layoutRes: Int by lazy { R.layout.fragment_chat_list }

}
