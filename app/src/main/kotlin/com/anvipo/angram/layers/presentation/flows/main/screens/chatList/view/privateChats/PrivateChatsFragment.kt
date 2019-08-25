package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.anvipo.angram.R
import com.anvipo.angram.layers.application.tdApiHelper.TdApiHelper
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.recyclerView.section.ChatListScreenSectionListAdapter
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModel
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelFactory
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelImpl
import kotlinx.android.synthetic.main.private_chats_fragment.*
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

    override fun setupUI() {
        recycler_view_private_chats_fragment.layoutManager = LinearLayoutManager(context!!)
        recycler_view_private_chats_fragment.adapter = adapter
        recycler_view_private_chats_fragment.setHasFixedSize(true)

//        TdApiHelper.chats
//        adapter.submitList()
//        println()
    }

    private val adapter by lazy { ChatListScreenSectionListAdapter() }

}
