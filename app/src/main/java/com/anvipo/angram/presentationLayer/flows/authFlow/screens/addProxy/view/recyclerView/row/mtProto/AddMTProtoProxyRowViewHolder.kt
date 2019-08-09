package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view.recyclerView.row.mtProto

import android.text.Editable
import android.text.TextWatcher
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
        onTextChangedHelper = row.onTextChanged
        editText.inputType = row.textInputType

        editText.addTextChangedListener(textWatcher)
    }


    private val editTextInputLayout: TextInputLayout = itemView.add_proxy_text_input_layout
    private val editText: TextInputEditText = editTextInputLayout.add_proxy_edit_text

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?): Unit = Unit

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ): Unit = Unit

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) = onTextChangedHelper!!(s)
    }

    private var onTextChangedHelper: ((CharSequence?) -> Unit)? = null

}