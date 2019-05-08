package com.anvipo.angram.presentationLayer.common.interfaces

interface CoordinatorOutput : Coordinatorable {

    var finishFlow: (() -> Unit)?

}