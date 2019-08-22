package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModel
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelFactory
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelImpl
import org.koin.android.ext.android.get

class PrivateChatsFragment : BaseFragment() {

    companion object {
        fun createNewInstance(): PrivateChatsFragment = PrivateChatsFragment()
    }

    override val actionBarTitle: String by lazy {
        getString(R.string.private_chats_title)
    }
    override val actionBar: Toolbar
        get() = (parentFragment as BaseFragment).actionBar

    override val layoutRes: Int by lazy { R.layout.private_chats_fragment }

    override val viewModel: PrivateChatsViewModel by viewModels<PrivateChatsViewModelImpl> {
        get<PrivateChatsViewModelFactory>()
    }

}
