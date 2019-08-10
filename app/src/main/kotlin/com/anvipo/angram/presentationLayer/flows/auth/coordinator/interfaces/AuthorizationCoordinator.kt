package com.anvipo.angram.presentationLayer.flows.auth.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.BaseCoordinator
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.types.AuthorizationCoordinateResult

@Suppress("RedundantUnitReturnType")
interface AuthorizationCoordinator : BaseCoordinator<AuthorizationCoordinateResult> {

    suspend fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen(): AuthorizationCoordinateResult
    suspend fun startAuthFlowWithEnterPasswordAsRootScreen(): AuthorizationCoordinateResult

}
