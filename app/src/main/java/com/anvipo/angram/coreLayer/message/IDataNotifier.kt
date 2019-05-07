package com.anvipo.angram.coreLayer.message

import kotlinx.coroutines.CancellationException

interface IDataNotifier<T> {
    fun closeChannel(cause: CancellationException? = null)
}
