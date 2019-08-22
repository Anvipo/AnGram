package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces

import com.anvipo.angram.layers.core.base.interfaces.BaseCoordinator
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.types.AuthorizationCoordinateResult
import org.drinkless.td.libcore.telegram.TdApi

interface AuthorizationCoordinator : BaseCoordinator<AuthorizationCoordinateResult> {

    fun set(startAuthorizationState: TdApi.UpdateAuthorizationState)

}
