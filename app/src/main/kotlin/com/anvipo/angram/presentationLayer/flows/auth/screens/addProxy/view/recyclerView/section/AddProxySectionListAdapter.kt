package com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.section

import com.anvipo.angram.presentationLayer.common.baseClasses.view.section.SectionListAdapter
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.section.base.AddProxySection
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.section.base.AddProxySectionViewHolder

class AddProxySectionListAdapter : SectionListAdapter<
        AddProxySection,
        AddProxySectionViewHolder<AddProxySection>
        >(AddProxySectionDiffCallback())