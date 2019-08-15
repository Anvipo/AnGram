package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section

import com.anvipo.angram.layers.core.base.classes.section.SectionListAdapter
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section.base.AddProxySection
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section.base.AddProxySectionViewHolder

class AddProxySectionListAdapter : SectionListAdapter<
        AddProxySection,
        AddProxySectionViewHolder<AddProxySection>
        >(AddProxySectionDiffCallback())