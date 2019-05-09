package com.anvipo.angram.applicationLayer.navigation.coordinator

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput

interface ApplicationCoordinatorOutput : CoordinatorOutput {

    fun coldStartApp()
    fun hotStartApp() {}

}
