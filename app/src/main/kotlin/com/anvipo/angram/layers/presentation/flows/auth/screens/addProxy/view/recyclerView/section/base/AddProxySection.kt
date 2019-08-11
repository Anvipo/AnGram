package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section.base

import com.anvipo.angram.layers.presentation.common.interfaces.view.section.BaseSection
import com.anvipo.angram.layers.presentation.common.interfaces.view.section.SectionViewData
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section.mtProto.AddMTProtoProxySectionViewData

abstract class AddProxySection : BaseSection {

    final override val viewsData: List<SectionViewData> =
        listOf(AddMTProtoProxySectionViewData)

}
