package com.anvipo.angram.applicationLayer.navigation.coordinator

import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.assertionFailure
import com.anvipo.angram.coreLayer.message.SystemMessageNotifier
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router

class ApplicationCoordinatorImp(
    private val router: Router,
    private val coordinatorsFactory: ApplicationCoordinatorsFactory,
    private val tdLibGateway: TDLibGateway,
    private val systemMessageNotifier: SystemMessageNotifier
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
        val authCoordinator = coordinatorsFactory.createAuthCoordinator(
            router = router,
            tdLibGateway = tdLibGateway
        )

        authCoordinator.finishFlow = {
            removeChildCoordinator(coordinator = authCoordinator)
            startMainFlow()
        }

        addChildCoordinator(coordinator = authCoordinator)

        authCoordinator.coldStart()
    }

    private fun startMainFlow() {
        val mainCoordinator = coordinatorsFactory.createMainCoordinator(
            router = router,
            tdLibGateway = tdLibGateway
        )

        addChildCoordinator(coordinator = mainCoordinator)

        mainCoordinator.coldStart()
    }


    private fun onFailureGetAuthStateResult(tag: String): (Throwable) -> Unit = { error ->
        // TODO: handle this case

        val message = "$tag: authStateResult.onFailure: ${error.localizedMessage}"

        systemMessageNotifier.send(message)

        startAuthFlow()
    }

    private fun onSuccessGetAuthStateResult(tag: String): (TdApi.AuthorizationState) -> Unit = { authState ->
        when (authState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val message = "$tag: onSuccessGetAuthStateResult: TDLib waits parameters"

                systemMessageNotifier.send(message)

                startAuthFlow()
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                val message = "$tag: onSuccessGetAuthStateResult: TDLib waits phone number"

                systemMessageNotifier.send(message)

                startAuthFlow()
            }
            is TdApi.AuthorizationStateWaitCode -> {
                val message = "$tag: onSuccessGetAuthStateResult: TDLib waits code"

                systemMessageNotifier.send(message)

                startAuthFlow()
            }
            else -> {
                // TODO: handle this case

                val message = "$tag: onSuccessGetAuthStateResult; TdApi.AuthorizationState: $authState"

                systemMessageNotifier.send(message)

                assertionFailure()
            }
        }
    }

}
