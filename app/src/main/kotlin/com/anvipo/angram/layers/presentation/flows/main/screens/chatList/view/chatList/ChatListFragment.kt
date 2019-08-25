package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commitNow
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.navigation.PrivateChatsScreen
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.chatList.ChatListViewModel
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.chatList.ChatListViewModelFactory
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.chatList.ChatListViewModelImpl
import kotlinx.android.synthetic.main.fragment_chat_list.*
import org.koin.android.ext.android.get
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ChatListFragment : BaseFragment() {

    companion object {
        fun createNewInstance(): ChatListFragment =
            ChatListFragment()
    }

    override fun setupUI() {
        val tab = when (currentTabFragment?.tag) {
            PrivateChatsScreen.screenKey -> PrivateChatsScreen
            else -> PrivateChatsScreen
        }

        selectTab(tab)
    }

    override fun setupListeners() {
        chat_list_bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_private_chats -> {
                }
                R.id.action_basic_groups -> {
//                    setActionBarTitle(resources.getString(R.string.basic_groups_title))
                }
                R.id.action_supergroups -> {
//                    setActionBarTitle(resources.getString(R.string.supergroups_title))
                }
                R.id.action_secret_chats -> {
//                    setActionBarTitle(resources.getString(R.string.secret_chats_title))
                }
                R.id.action_other_chats -> {
//                    setActionBarTitle(resources.getString(R.string.other_chats_title))
                }
            }

            return@setOnNavigationItemSelectedListener true
        }
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

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment

        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) {
            return
        }

        childFragmentManager.commitNow {
            if (newFragment == null) {
                add(
                    R.id.chat_list_screen_container,
                    tab.fragment,
                    tab.screenKey
                )
            }

            currentFragment?.also {
                hide(it)
                setMaxLifecycle(it, Lifecycle.State.STARTED)
            }
            newFragment?.also {
                show(it)
                setMaxLifecycle(it, Lifecycle.State.RESUMED)
            }
        }
    }

}
