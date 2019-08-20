package com.anvipo.angram.layers.global

import com.anvipo.angram.layers.core.base.interfaces.HasMyCoroutineBuilders
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface HasCheckAuthorizationStateHelper<T> : HasMyCoroutineBuilders {

    suspend fun checkAuthorizationState(
        tdApiUpdateAuthorizationStateReceiveChannel: TdApiUpdateAuthorizationStateReceiveChannel
    ): T = suspendCoroutine {
        onCreatedCheckAuthorizationStateContinuation(it)

        myLaunch(Dispatchers.IO) {
            for (receivedTdApiUpdateAuthorizationState in tdApiUpdateAuthorizationStateReceiveChannel) {
                onReceivedTdApiUpdateAuthorizationState(receivedTdApiUpdateAuthorizationState)
            }
        }
    }

    fun onCreatedCheckAuthorizationStateContinuation(
        checkAuthorizationStateContinuation: Continuation<T>
    )

    suspend fun onReceivedTdApiUpdateAuthorizationState(
        receivedUpdateAuthorizationState: TdApi.UpdateAuthorizationState
    )

    fun finishFlow(
        tdApiUpdateAuthorizationStateReceiveChannel: TdApiUpdateAuthorizationStateReceiveChannel,
        finishFlow: Continuation<T>,
        flowCoordinateResult: T
    ) {
        tdApiUpdateAuthorizationStateReceiveChannel.cancel(
            CancellationException("Finish flow")
        )
        finishFlow.resume(flowCoordinateResult)
    }

}