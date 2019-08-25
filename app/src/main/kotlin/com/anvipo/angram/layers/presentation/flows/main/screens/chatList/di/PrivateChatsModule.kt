package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.di

import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule.mainCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.navigation.PrivateChatsScreen
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.PrivateChatsFragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModel
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelFactory
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelImpl
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.dsl.module

object PrivateChatsModule {

    val module: Module = module {

        scope(mainCoordinatorScopeQualifier) {
//            scoped<TdApiUpdateConnectionStateSendChannel>(
//                tdApiUpdateConnectionStateAuthorizationFlowSendChannelQualifier
//            ) {
//                authorizationCoordinatorScope!!.get(
//                    tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannelQualifier
//                )
//            }
//            factory(
//                tdApiUpdateConnectionStateAuthorizationFlowReceiveChannelQualifier
//            ) {
//                authorizationCoordinatorScope!!.get<TdApiUpdateConnectionStateBroadcastChannel>(
//                    tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannelQualifier
//                ).openSubscription()
//            }
//            scoped<TdApiUpdateConnectionStateBroadcastChannel>(
//                tdApiUpdateConnectionStateAuthorizationFlowBroadcastChannelQualifier
//            ) {
//                BroadcastChannel(Channel.CONFLATED)
//            }
        }


        factory {
            PrivateChatsFragment.createNewInstance()
        }

        factory {
            PrivateChatsScreen
        }

        single {
            PrivateChatsViewModelFactory
        }

        factory<PrivateChatsViewModel> {
            PrivateChatsViewModelImpl(
            )
        }

    }

}