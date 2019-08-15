package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.mtProto

import android.view.View
import com.anvipo.angram.layers.core.textWatchers.TextWatcherImp
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.base.AddProxyRowViewHolder
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

    private val textWatcher by lazy {
        TextWatcherImp(
            onEnteredText = { onTextChangedHelper!!(it) }
        )
    }

    private var onTextChangedHelper: ((CharSequence?) -> Unit)? = null

}