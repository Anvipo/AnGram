package com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces

import com.anvipo.angram.layers.presentation.common.interfaces.BaseCoordinator
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.types.AuthorizationCoordinateResult

@Suppress("RedundantUnitReturnType")
interface AuthorizationCoordinator : BaseCoordinator<AuthorizationCoordinateResult> {

    suspend fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen(): AuthorizationCoordinateResult
    suspend fun startAuthFlowWithEnterPasswordAsRootScreen(): AuthorizationCoordinateResult

}
