package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.mtProto

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.AddProxyRowListAdapter

class AddMTProtoProxyRowListAdapter :
    AddProxyRowListAdapter<
            AddMTProtoProxyRow,
            AddMTProtoProxyRowViewHolder
            >(AddMTProtoProxyRowDiffCallback())