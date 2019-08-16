package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.section.base

import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.BaseSection
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.section.SectionViewData
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.section.mtProto.AddMTProtoProxySectionViewData

abstract class AddProxySection : BaseSection {

    final override val viewsData: List<SectionViewData> =
        listOf(AddMTProtoProxySectionViewData)

}
