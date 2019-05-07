package com.anvipo.angram.coreLayer.message

interface ISentDataNotifier<DataType> : IDataNotifier<DataType> {
    fun send(data: DataType)
}