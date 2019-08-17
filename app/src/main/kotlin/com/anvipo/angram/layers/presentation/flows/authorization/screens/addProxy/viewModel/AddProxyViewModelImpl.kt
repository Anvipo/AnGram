package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.addProxy.AddProxyUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.ShowErrorEvent
import com.anvipo.angram.layers.core.events.ShowAlertMessageEvent
import com.anvipo.angram.layers.core.events.ShowViewEvent
import com.anvipo.angram.layers.core.events.ShowViewEvent.HIDE
import com.anvipo.angram.layers.core.events.ShowViewEvent.SHOW
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi

class AddProxyViewModelImpl(
    private val routeEventHandler: AuthorizationCoordinatorAddProxyRouteEventHandler,
    private val useCase: AddProxyUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModelImpl(), AddProxyViewModel {

    override val showAddProxyEvents: SingleLiveEvent<ShowViewEvent> =
        SingleLiveEvent()

    override fun coldStart() {
        // TODO: uncomment
//        hideAddProxyButton()
    }

    override fun addProxyButtonTapped() {
        showProgress()

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
                        hideProgress()
                        showAlertMessage(
                            ShowAlertMessageEvent(
                                title = null,
                                text = resourceManager.getString(R.string.proxy_successfully_has_been_added),
                                cancelable = false,
                                messageDialogTag = addProxySuccessTag
                            )
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
                        hideProgress()
                        showErrorAlert(ShowErrorEvent(text = errorMessage))
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
                showAddProxyButton()
            } else {
                hideAddProxyButton()
            }
        }
    private var port: Int = 0
        set(value) {
            field = value
            if (serverAddress.isNotEmpty() && field != 0) {
                showAddProxyButton()
            } else {
                hideAddProxyButton()
            }
        }

    private var secretText: String = ""
        set(value) {
            field = value
            (this.proxyType as TdApi.ProxyTypeMtproto).secret = field
        }

    private fun showAddProxyButton() {
        showAddProxyEvents.value = SHOW
    }

    private fun hideAddProxyButton() {
        showAddProxyEvents.value = HIDE
    }

}