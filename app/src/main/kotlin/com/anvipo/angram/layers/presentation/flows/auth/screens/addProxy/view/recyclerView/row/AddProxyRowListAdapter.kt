package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.anvipo.angram.layers.presentation.common.baseClasses.row.RowListAdapter
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.base.AddProxyRow
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.base.AddProxyRowViewHolder

@Suppress("unused")
abstract class AddProxyRowListAdapter<
        APR : AddProxyRow,
        RVH : AddProxyRowViewHolder<APR>
        > : RowListAdapter<APR, RVH> {

    constructor(
        diffCallback: DiffUtil.ItemCallback<APR>
    ) : super(diffCallback)

    constructor(
        config: AsyncDifferConfig<APR>
    ) : super(config)

}