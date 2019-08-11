package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.addProxy.AddProxyUseCase
import com.anvipo.angram.layers.core.CoreHelpers.debugLog
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.presentation.common.baseClasses.BasePresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.AddProxyView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        // TODO: uncomment
//        viewState.hideAddProxyButton()
    }

    override fun addProxyButtonTapped() {
        val addProxyButtonTappedCEH = CoroutineExceptionHandler { _, error ->
            val errorText = error.localizedMessage
            debugLog(errorText)
            viewState.showErrorAlert(errorText)
        }

        viewState.showProgress()

        launch(
            context = coroutineContext + addProxyButtonTappedCEH
        ) {
            useCase
                .addProxyCatching(
                    server = "tg-2.rknsosatb.pw",
                    port = 443,
                    type = TdApi.ProxyTypeMtproto("dde99993ad3d7146fcf8f3baa789cc62ac")
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
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
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
        this.portText = portText?.toString()?.toIntOrNull() ?: 0
    }

    override fun onSecretTextChanged(secretText: CharSequence?) {
        this.secretText = secretText?.toString() ?: ""
    }

    override fun setProxyType(proxyType: TdApi.ProxyType) {
        this.proxyType = proxyType
    }


    override val coroutineContext: CoroutineContext = Dispatchers.IO


    private val addProxySuccessTag = "addProxySuccessTag"

    private var proxyType: TdApi.ProxyType? = null
    private var serverText: String = ""
        set(value) {
            field = value
            if (field.isNotEmpty() && portText != 0) {
                viewState.showAddProxyButton()
            } else {
                viewState.hideAddProxyButton()
            }
        }
    private var portText: Int = 0
        set(value) {
            field = value
            if (serverText.isNotEmpty() && field != 0) {
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

}