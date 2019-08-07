package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.mtProto

import android.view.View
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.base.AddProxyRowViewHolder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_add_proxy.view.*

class AddMTProtoProxyRowViewHolder(
    itemView: View
) : AddProxyRowViewHolder<AddMTProtoProxyRow>(itemView) {

    override fun bind(row: AddMTProtoProxyRow) {
        editTextInputLayout.hint = row.textInputHint
    }

    private val editTextInputLayout: TextInputLayout = itemView.add_proxy_text_input_layout
    private val editText: TextInputEditText = editTextInputLayout.add_proxy_edit_text

}