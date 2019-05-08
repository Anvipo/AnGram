package com.anvipo.angram.presentationLayer.common.interfaces

interface BasePresenter {

    fun coldStart(): Unit = Unit
    fun hotStart(): Unit = Unit

    fun onBackPressed(): Unit = Unit

    fun onCanceledProgressDialog(): Unit = Unit

}