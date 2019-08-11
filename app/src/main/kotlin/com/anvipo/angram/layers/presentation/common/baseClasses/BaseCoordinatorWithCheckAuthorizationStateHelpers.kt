package com.anvipo.angram.layers.presentation.common.baseClasses

import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.data.gateways.tdLib.base.BaseTdLibGatewayWithGetAuthorizationState
import org.drinkless.td.libcore.telegram.TdApi

abstract class BaseCoordinatorWithCheckAuthorizationStateHelpers<out CoordinateResultType>(
    private val tdLibGateway: BaseTdLibGatewayWithGetAuthorizationState,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImp<CoordinateResultType>(
    systemMessageSendChannel = systemMessageSendChannel
) {

    protected fun checkAuthorizationStateHelper() {
        checkAuthorizationStateHelperWithGeneric<TdApi.AuthorizationState>()
    }

    protected abstract fun onSuccessGetAuthorizationStateResult(authState: TdApi.AuthorizationState)
    protected abstract fun onFailureGetAuthorizationStateResult(error: Throwable)

    private inline fun <reified T : TdApi.AuthorizationState> checkAuthorizationStateHelperWithGeneric(
        noinline onFailure: ((Throwable) -> Unit)? = null,
        noinline onSuccess: ((T) -> Unit)? = null
    ) {
        myLaunch {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateCatching()

            authorizationStateResult
                .onSuccess {
                    if (onSuccess != null && it is T) {
                        onSuccess.invoke(it)
                    } else {
                        onSuccessGetAuthorizationStateResult(it)
                    }
                }
                .onFailure {
                    if (onFailure != null) {
                        onFailure.invoke(it)
                    } else {
                        onFailureGetAuthorizationStateResult(it)
                    }
                }
        }
    }

}