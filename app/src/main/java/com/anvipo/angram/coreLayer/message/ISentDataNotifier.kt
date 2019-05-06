package com.anvipo.angram.coreLayer.message

interface ISentDataNotifier<DataType> {
    fun send(data: DataType)
}