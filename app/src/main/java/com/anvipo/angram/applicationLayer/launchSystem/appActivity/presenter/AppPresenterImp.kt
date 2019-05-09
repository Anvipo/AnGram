package com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter

import android.util.Log
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.view.AppView
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinatorOutput
import com.anvipo.angram.applicationLayer.types.SystemMessageReceiveChannel
import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import com.anvipo.angram.coreLayer.CoreHelpers
import com.anvipo.angram.coreLayer.message.SystemMessageType
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class AppPresenterImp(
    override val coordinator: ApplicationCoordinatorOutput,
    private val systemMessageReceiveChannel: SystemMessageReceiveChannel
) : BasePresenterImp<AppView>(), AppPresenter {

    override fun onPause() {
        viewState.removeNavigator()
        unsubscribeOnSystemMessages()
    }

    override fun onResumeFragments() {
        subscribeOnSystemMessages()
        viewState.setNavigator()
    }

    override fun coldStart() {
        coordinator.coldStart()
    }

    override fun hotStart() {
        coordinator.hotStart()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun cancelAllJobs() {
        receiveSystemMessagesJob?.cancel()
    }

    override val useCase: BaseUseCase
        get() = TODO("not implemented")

    private fun subscribeOnSystemMessages() {
        val receiveSystemMessagesCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val text = error.localizedMessage
                CoreHelpers.debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        receiveSystemMessagesJob = launch(
            context = coroutineContext + receiveSystemMessagesCoroutineExceptionHandler
        ) {
            val (text, type, shouldBeShownToUser, shouldBeShownInLogs) = systemMessageReceiveChannel.receive()

            if (shouldBeShownToUser) {
                when (type) {
                    SystemMessageType.TOAST -> {
                        launch(
                            context = Dispatchers.Main + receiveSystemMessagesCoroutineExceptionHandler
                        ) {
                            viewState.showToastMessage(text)
                        }
                    }
                    SystemMessageType.ALERT -> {
                        launch(
                            context = Dispatchers.Main + receiveSystemMessagesCoroutineExceptionHandler
                        ) {
                            viewState.showToastMessage(text)
                        }
                    }
                }
            }

            if (shouldBeShownInLogs) {
                Log.d(App.TAG, text)
            }
        }
    }

    private fun unsubscribeOnSystemMessages() {
        systemMessageReceiveChannel.cancel(CancellationException("AppActivity onPause"))
    }

    private var receiveSystemMessagesJob: Job? = null

}