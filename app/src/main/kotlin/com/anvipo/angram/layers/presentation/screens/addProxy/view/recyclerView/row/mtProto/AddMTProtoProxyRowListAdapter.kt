package com.anvipo.angram.layers.presentation.screens.addProxy.view.recyclerView.row.mtProto

import com.anvipo.angram.layers.presentation.screens.addProxy.view.recyclerView.row.AddProxyRowListAdapter

class AddMTProtoProxyRowListAdapter :
    AddProxyRowListAdapter<
            AddMTProtoProxyRow,
            AddMTProtoProxyRowViewHolder
            >(AddMTProtoProxyRowDiffCallback())