package com.anvipo.angram.coreLayer.message

import kotlinx.coroutines.channels.ReceiveChannel

interface IReceiveDataNotifier<DataType> : IDataNotifier<DataType> {
    val receiveChannel: ReceiveChannel<DataType>
}