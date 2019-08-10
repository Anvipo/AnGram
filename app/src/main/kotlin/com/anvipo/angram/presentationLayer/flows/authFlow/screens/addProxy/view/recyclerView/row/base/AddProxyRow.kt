package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.base

import com.anvipo.angram.presentationLayer.common.interfaces.view.row.BaseRow
import com.anvipo.angram.presentationLayer.common.interfaces.view.row.RowViewData
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.mtProto.AddMTProtoProxyRowViewData

abstract class AddProxyRow : BaseRow {

    override val viewsData: List<RowViewData> =
        listOf(AddMTProtoProxyRowViewData)

}