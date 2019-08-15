package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.mtProto

import android.text.InputType
import com.anvipo.angram.layers.core.IndexPath
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.base.AddProxyRow

class AddMTProtoProxyRow(
    val textInputHint: String,
    val textInputType: Int = InputType.TYPE_CLASS_TEXT,
    val onTextChanged: (CharSequence?) -> Unit,
    override val indexPath: IndexPath
) : AddProxyRow() {

    override val viewType: Int = AddMTProtoProxyRowViewData.viewType

}