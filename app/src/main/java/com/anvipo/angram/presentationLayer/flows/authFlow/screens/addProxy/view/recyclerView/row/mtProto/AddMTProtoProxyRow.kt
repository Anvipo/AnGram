package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.mtProto

import com.anvipo.angram.coreLayer.IndexPath
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.base.AddProxyRow

@Suppress("EXPERIMENTAL_API_USAGE")
class AddMTProtoProxyRow(
    val textInputHint: String,
    override val indexPath: IndexPath
) : AddProxyRow() {

    override val viewType: Int = AddMTProtoProxyRowViewData.viewType

}