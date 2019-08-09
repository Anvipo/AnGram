package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.section.base

import com.anvipo.angram.presentationLayer.common.interfaces.view.section.BaseSection
import com.anvipo.angram.presentationLayer.common.interfaces.view.section.SectionViewData
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.section.mtProto.AddMTProtoProxySectionViewData

abstract class AddProxySection : BaseSection {

    override val viewsData: List<SectionViewData> =
        listOf(AddMTProtoProxySectionViewData)

}
