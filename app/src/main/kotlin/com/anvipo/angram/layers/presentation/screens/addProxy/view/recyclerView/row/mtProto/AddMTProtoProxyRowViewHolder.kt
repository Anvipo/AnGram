package com.anvipo.angram.layers.presentation.screens.addProxy.view.recyclerView.row.mtProto

import android.view.View
import androidx.lifecycle.observe
import com.anvipo.angram.layers.core.textWatchers.TextWatcherImpl
import com.anvipo.angram.layers.presentation.screens.addProxy.view.AddProxyFragment.Companion.authenticationKey
import com.anvipo.angram.layers.presentation.screens.addProxy.view.AddProxyFragment.Companion.serverAddress
import com.anvipo.angram.layers.presentation.screens.addProxy.view.AddProxyFragment.Companion.serverPort
import com.anvipo.angram.layers.presentation.screens.addProxy.view.recyclerView.row.base.AddProxyRowViewHolder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_add_proxy.view.*

class AddMTProtoProxyRowViewHolder(
    itemView: View
) : AddProxyRowViewHolder<AddMTProtoProxyRow>(itemView) {

    @ExperimentalUnsignedTypes
    override fun bind(row: AddMTProtoProxyRow) {
        editTextInputLayout.hint = row.textInputHint
        onTextChangedHelper = row.onTextChanged
        editText.inputType = row.textInputType
        row
            .addProxyScreenSavedInputDataEvents
            .observe(this) {
                when (row.indexPath) {
                    serverAddress -> editText.setText(it.serverAddress)
                    serverPort -> editText.setText(it.serverPort?.toString())
                    authenticationKey -> editText.setText(it.authenticationKey)
                }
            }

        editText.addTextChangedListener(textWatcher)
    }


    private val editTextInputLayout: TextInputLayout = itemView.add_proxy_text_input_layout
    private val editText: TextInputEditText = editTextInputLayout.add_proxy_edit_text

    private val textWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { onTextChangedHelper!!(it) }
        )
    }

    private var onTextChangedHelper: ((CharSequence?) -> Unit)? = null

}