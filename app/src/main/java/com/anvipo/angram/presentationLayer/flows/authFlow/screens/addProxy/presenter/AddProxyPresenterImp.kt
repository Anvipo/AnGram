package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.presenter

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.addProxyUseCase.AddProxyUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.AddProxyView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
@InjectViewState
class AddProxyPresenterImp(
    private val routeEventHandler: AuthorizationCoordinatorAddProxyRouteEventHandler,
    private val useCase: AddProxyUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImp<AddProxyView>(), AddProxyPresenter {

    override fun coldStart() {
        viewState.hideAddProxyButton()
    }

    override fun addProxyButtonTapped() {
        val addProxyButtonTappedCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        addProxyButtonTappedJob = launch(
            context = coroutineContext + addProxyButtonTappedCEH
        ) {
            useCase
                .addProxyCatching(
                    server = serverText,
                    port = portText.toInt(),
                    type = proxyType!!
                )
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showAlertMessage(
                            resourceManager.getString(R.string.proxy_successfully_has_been_added),
                            cancelable = false,
                            messageDialogTag = addProxySuccessTag
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        if (error is TdApiError && error.message != null) {
                            error.message!!
                        } else {
                            getString(R.string.unknown_error)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun messageDialogPositiveClicked(tag: String) {
        if (tag == addProxySuccessTag) {
            routeEventHandler.onSuccessAddProxy()
        }
    }

    override fun onBackPressed() {
        routeEventHandler.onPressedBackButtonInAddProxyScreen()
    }


    override fun onServerTextChanged(serverText: CharSequence?) {
        this.serverText = serverText?.toString() ?: ""
    }

    override fun onPortTextChanged(portText: CharSequence?) {
        this.portText = portText?.toString()?.toUIntOrNull() ?: 0u
    }

    override fun onSecretTextChanged(secretText: CharSequence?) {
        this.secretText = secretText?.toString() ?: ""
    }

    override fun setProxyType(proxyType: TdApi.ProxyType) {
        this.proxyType = proxyType
    }


    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun cancelAllJobs() {
        addProxyButtonTappedJob?.cancel()
    }

    private val addProxySuccessTag = "addProxySuccessTag"

    private var proxyType: TdApi.ProxyType? = null
    private var serverText: String = ""
        set(value) {
            field = value
            if (field.isNotEmpty() && portText != 0u) {
                viewState.showAddProxyButton()
            } else {
                viewState.hideAddProxyButton()
            }
        }
    private var portText: UInt = 0u
        set(value) {
            field = value
            if (serverText.isNotEmpty() && field != 0u) {
                viewState.showAddProxyButton()
            } else {
                viewState.hideAddProxyButton()
            }
        }
    private var secretText: String = ""
        set(value) {
            field = value
            (this.proxyType as TdApi.ProxyTypeMtproto).secret = field
        }

    private var addProxyButtonTappedJob: Job? = null

}