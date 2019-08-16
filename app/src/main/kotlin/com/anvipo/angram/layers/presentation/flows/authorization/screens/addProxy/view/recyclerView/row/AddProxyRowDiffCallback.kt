package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.row

import com.anvipo.angram.layers.core.base.classes.recyclerView.row.BaseRowDiffCallback
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.row.base.AddProxyRow

abstract class AddProxyRowDiffCallback<APR : AddProxyRow> : BaseRowDiffCallback<APR>() {

    final override fun areContentsTheSame(
        oldItem: APR,
        newItem: APR
    ): Boolean = true

}