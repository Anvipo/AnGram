package com.anvipo.angram.presentationLayer.common.interfaces

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface BasePresenter : CoroutineScope {

    val job: Job
    val coroutineExceptionHandler: CoroutineExceptionHandler

    fun coldStart()

    fun onBackPressed()

    fun onDestroy()

}