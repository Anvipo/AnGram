package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.addProxy.AddProxyUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BasePresenterImpl
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.AddProxyView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi

@InjectViewState
class AddProxyPresenterImpl(
    private val routeEventHandler: AuthorizationCoordinatorAddProxyRouteEventHandler,
    private val useCase: AddProxyUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImpl<AddProxyView>(), AddProxyPresenter {

    override fun coldStart() {
        // TODO: uncomment
//        viewState.hideAddProxyButton()
    }

    override fun addProxyButtonTapped() {
        viewState.showProgress()

        myLaunch {
            val addProxyResult =
                withContext(Dispatchers.IO) {
                    useCase
                        .addProxyCatching(
                            server = "tg-2.rknsosatb.pw",
                            port = 443,
                            type = TdApi.ProxyTypeMtproto("dde99993ad3d7146fcf8f3baa789cc62ac")
                        )
                }

            addProxyResult
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
            myLaunch {
                routeEventHandler.onSuccessAddProxy()
            }
        }
    }

    override fun onBackPressed() {
        myLaunch {
            routeEventHandler.onPressedBackButtonInAddProxyScreen()
        }
    }


    override fun onServerTextChanged(serverText: CharSequence?) {
        this.serverAddress = serverText?.toString() ?: ""
    }

    override fun onPortTextChanged(portText: CharSequence?) {
        this.port = portText?.toString()?.toIntOrNull() ?: 0
    }

    override fun onSecretTextChanged(secretText: CharSequence?) {
        this.secretText = secretText?.toString() ?: ""
    }

    override fun setProxyType(proxyType: TdApi.ProxyType) {
        this.proxyType = proxyType
    }


    private val addProxySuccessTag = "addProxySuccessTag"

    private var proxyType: TdApi.ProxyType? = null
    private var serverAddress: String = ""
        set(value) {
            field = value
            if (field.isNotEmpty() && port != 0) {
                viewState.showAddProxyButton()
            } else {
                viewState.hideAddProxyButton()
            }
        }
    private var port: Int = 0
        set(value) {
            field = value
            if (serverAddress.isNotEmpty() && field != 0) {
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