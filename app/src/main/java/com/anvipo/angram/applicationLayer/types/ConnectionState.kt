package com.anvipo.angram.applicationLayer.types

enum class ConnectionState {

    WaitingForNetwork,
    ConnectingToProxy,
    Connecting,
    Updating,
    Ready,

    Undefined

}