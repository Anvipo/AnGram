package com.anvipo.angram.layers.presentation.screens.addProxy.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.addProxy.AddProxyUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import com.anvipo.angram.layers.presentation.screens.addProxy.types.AddProxyScreenSavedInputData
import com.anvipo.angram.layers.presentation.screens.addProxy.view.AddProxyFragment.Companion.ENTERED_AUTHENTICATION_KEY
import com.anvipo.angram.layers.presentation.screens.addProxy.view.AddProxyFragment.Companion.ENTERED_SERVER_ADDRESS
import com.anvipo.angram.layers.presentation.screens.addProxy.view.AddProxyFragment.Companion.ENTERED_SERVER_PORT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi

class AddProxyViewModelImpl(
    private val routeEventHandler: AuthorizationCoordinatorAddProxyRouteEventHandler,
    private val useCase: AddProxyUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModelImpl(), AddProxyViewModel {

    override val showAddProxyEvents: LiveData<ShowViewEventParameters> by lazy {
        _showAddProxyEvents
    }

    override val addProxyScreenSavedInputDataEvents: LiveData<AddProxyScreenSavedInputData> by lazy {
        _addProxyScreenSavedInputDataEvents
    }

    override val enteredServerAddress: LiveData<String?> by lazy {
        _enteredServerAddress
    }

    @ExperimentalUnsignedTypes
    override val enteredServerPort: LiveData<UInt?> by lazy {
        _enteredServerPort
    }

    override val enteredAuthenticationKey: LiveData<String?> by lazy {
        _enteredAuthenticationKey
    }

    override fun onColdStart() {
        super<BaseViewModelImpl>.onColdStart()
        hideAddProxyButton()
    }

    @ExperimentalUnsignedTypes
    override fun onHotStart(savedInstanceState: Bundle) {
        super<BaseViewModelImpl>.onHotStart(savedInstanceState)
        val enteredServerAddress = savedInstanceState.getString(ENTERED_SERVER_ADDRESS)
        val enteredServerPort = savedInstanceState.getString(ENTERED_SERVER_PORT)?.toUIntOrNull()
        val enteredAuthenticationKey = savedInstanceState.getString(ENTERED_AUTHENTICATION_KEY)
        _addProxyScreenSavedInputDataEvents.value =
            AddProxyScreenSavedInputData(
                serverAddress = enteredServerAddress,
                serverPort = enteredServerPort,
                authenticationKey = enteredAuthenticationKey
            )
    }

    @ExperimentalUnsignedTypes
    override fun addProxyButtonTapped() {
        myLaunch {
            showProgress()

            val addProxyResult =
                withContext(Dispatchers.IO) {
                    useCase
                        .addProxyCatching(
                            server = serverAddress!!,
                            port = serverPort!!.toInt(),
                            type = proxyType!!
                        )
                }

            addProxyResult
                .onSuccess {
                    hideProgress()
                    showAlertMessage(
                        ShowAlertMessageEventParameters(
                            title = null,
                            text = resourceManager.getString(R.string.proxy_successfully_has_been_added),
                            cancelable = false,
                            messageDialogTag = addProxySuccessTag
                        )
                    )
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        if (error is TdApiError && error.message != null) {
                            error.message!!
                        } else {
                            getString(R.string.unknown_error)
                        }
                    }

                    hideProgress()
                    showErrorAlert(
                        ShowErrorEventParameters(
                            text = errorMessage,
                            cancelable = true,
                            messageDialogTag = null
                        )
                    )
                }
        }
    }

    override fun messageDialogPositiveClicked(tag: String?) {
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


    @ExperimentalUnsignedTypes
    override fun onServerAddressChanged(serverText: CharSequence?) {
        this.serverAddress = serverText?.toString()
    }

    @ExperimentalUnsignedTypes
    override fun onServerPortChanged(portText: CharSequence?) {
        this.serverPort = portText?.toString()?.toUIntOrNull()
    }

    override fun onAuthenticationKeyChanged(secretText: CharSequence?) {
        this.authenticationKey = secretText?.toString()
    }

    override fun setProxyType(proxyType: TdApi.ProxyType) {
        this.proxyType = proxyType
    }


    private val _showAddProxyEvents = MutableLiveData<ShowViewEventParameters>()
    private val _addProxyScreenSavedInputDataEvents =
        MutableLiveData<AddProxyScreenSavedInputData>()
    private val _enteredServerAddress = MutableLiveData<String?>()
    @ExperimentalUnsignedTypes
    private val _enteredServerPort = MutableLiveData<UInt?>()
    private val _enteredAuthenticationKey = MutableLiveData<String?>()

    private val addProxySuccessTag = "addProxySuccessTag"

    private var proxyType: TdApi.ProxyType? = null
    @ExperimentalUnsignedTypes
    private var serverAddress: String? = null
        set(value) {
            field = value
            _enteredServerAddress.value = field
            if (field?.isNotEmpty() == true && serverPort != null && serverPort != 0u) {
                showAddProxyButton()
            } else {
                hideAddProxyButton()
            }
        }
    @ExperimentalUnsignedTypes
    private var serverPort: UInt? = null
        set(value) {
            field = value
            _enteredServerPort.value = field
            if (serverAddress?.isNotEmpty() == true && field != null && field != 0u) {
                showAddProxyButton()
            } else {
                hideAddProxyButton()
            }
        }

    private var authenticationKey: String? = null
        set(value) {
            field = value
            _enteredAuthenticationKey.value = field
            (this.proxyType as TdApi.ProxyTypeMtproto).secret = field
        }

    private fun showAddProxyButton() {
        _showAddProxyEvents.value = SHOW
    }

    private fun hideAddProxyButton() {
        _showAddProxyEvents.value = HIDE
    }

}