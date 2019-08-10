package com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.section.mtProto

import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.headersAndFooters.base.AddProxyFooterData
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.headersAndFooters.mtProto.AddMTProtoProxyHeaderData
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.row.mtProto.AddMTProtoProxyRow
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.section.base.AddProxySection

@Suppress("EXPERIMENTAL_API_USAGE")
class AddMTProtoProxySection(
    override val index: UInt,
    override val items: List<AddMTProtoProxyRow>,
    override val headerData: AddMTProtoProxyHeaderData?,
    override val footerData: AddProxyFooterData? = null,
    override val viewType: Int = AddMTProtoProxySectionViewData.viewType
) : AddProxySection()