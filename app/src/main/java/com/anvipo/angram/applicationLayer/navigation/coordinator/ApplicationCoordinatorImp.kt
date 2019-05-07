package com.anvipo.angram.applicationLayer.navigation.coordinator

import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.assertionFailure
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorInput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorOutput
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi

class ApplicationCoordinatorImp(
    private val tdLibGateway: TDLibGateway,
    private val systemMessageSendChannel: SystemMessageSendChannel,
    private val authorizationCoordinator: AuthorizationCoordinatorInput,
    private val mainCoordinator: MainCoordinator
) : BaseCoordinator(), ApplicationCoordinator {

    override fun coldStart() {
        childCoordinators.clear()

        configureApp()
    }

    /**
    Configure and starts app
     */
    private fun configureApp() {
        startApp()
    }

    /**
    Starts needed flow
     */
    private fun startApp() {
        val tag = "${this::class.java.simpleName} startApp"

        GlobalScope.launch {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess(onSuccessGetAuthStateResult(tag))
                .onFailure(onFailureGetAuthStateResult(tag))
        }
    }

    private fun startAuthFlow() {
        (authorizationCoordinator as AuthorizationCoordinatorOutput).finishFlow = {
            removeChildCoordinator(coordinator = authorizationCoordinator)
            startMainFlow()
        }

        addChildCoordinator(coordinator = authorizationCoordinator)

        authorizationCoordinator.coldStart()
    }

    private fun startMainFlow() {
        addChildCoordinator(coordinator = mainCoordinator)

        mainCoordinator.coldStart()
    }


    private fun onFailureGetAuthStateResult(tag: String): (Throwable) -> Unit = { error ->
        // TODO: handle this case

        val text = "$tag: authStateResult.onFailure: ${error.localizedMessage}"

        systemMessageSendChannel.offer(SystemMessage(text = text))

        startAuthFlow()
    }

    private fun onSuccessGetAuthStateResult(tag: String): (TdApi.AuthorizationState) -> Unit = { authState ->
        when (authState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val text = "$tag: onSuccessGetAuthStateResult: TDLib waits parameters"

                systemMessageSendChannel.offer(SystemMessage(text = text))

                startAuthFlow()
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                val text = "$tag: onSuccessGetAuthStateResult: TDLib waits phone number"

                systemMessageSendChannel.offer(SystemMessage(text = text))

                startAuthFlow()
            }
            is TdApi.AuthorizationStateWaitCode -> {
                val text = "$tag: onSuccessGetAuthStateResult: TDLib waits code"

                systemMessageSendChannel.offer(SystemMessage(text = text))

                startAuthFlow()
            }
            else -> {
                // TODO: handle this case

                val text = "$tag: onSuccessGetAuthStateResult; TdApi.AuthorizationState: $authState"

                systemMessageSendChannel.offer(SystemMessage(text = text))

                assertionFailure()
            }
        }
    }

}
