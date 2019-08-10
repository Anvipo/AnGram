package com.anvipo.angram.layers.presentation.common

import android.text.Editable
import android.text.TextWatcher

class TextWatcherImp(
    private val onEnteredText: ((String) -> Unit)? = null
) : TextWatcher {

    override fun afterTextChanged(s: Editable?): Unit = Unit

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ): Unit = Unit

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        onEnteredText?.invoke(s.toString())
    }

}