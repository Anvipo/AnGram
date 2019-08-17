package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.row.mtProto

import android.text.InputType
import androidx.lifecycle.LiveData
import com.anvipo.angram.layers.core.IndexPath
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.AddProxyScreenSavedInputData
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.row.base.AddProxyRow

class AddMTProtoProxyRow(
    override val indexPath: IndexPath,
    val textInputHint: String,
    val textInputType: Int = InputType.TYPE_CLASS_TEXT,
    val onTextChanged: (CharSequence?) -> Unit,
    val addProxyScreenSavedInputDataEvents: LiveData<AddProxyScreenSavedInputData>
) : AddProxyRow() {

    override val viewType: Int = AddMTProtoProxyRowViewData.viewType

}