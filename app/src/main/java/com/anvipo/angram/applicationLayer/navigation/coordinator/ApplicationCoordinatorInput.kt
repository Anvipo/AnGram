package com.anvipo.angram.applicationLayer.navigation.coordinator

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorInput

interface ApplicationCoordinatorInput : CoordinatorInput {

    override fun coldStart() {}
}
