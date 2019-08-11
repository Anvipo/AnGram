package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.base

import com.anvipo.angram.layers.presentation.common.interfaces.view.row.BaseRow
import com.anvipo.angram.layers.presentation.common.interfaces.view.row.RowViewData
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.mtProto.AddMTProtoProxyRowViewData

abstract class AddProxyRow : BaseRow {

    final override val viewsData: List<RowViewData> =
        listOf(AddMTProtoProxyRowViewData)

}