package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row

import com.anvipo.angram.presentationLayer.common.baseClasses.view.row.BaseRowDiffCallback
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.base.AddProxyRow

abstract class AddProxyRowDiffCallback<APR : AddProxyRow> : BaseRowDiffCallback<APR>() {

    final override fun areContentsTheSame(
        oldItem: APR,
        newItem: APR
    ): Boolean = true

}