package com.anvipo.angram.presentationLayer.common

import android.telephony.PhoneNumberFormattingTextWatcher

class PhoneNumberTextWatcher(
    private val onEnteredCleanedPhoneNumber: ((String) -> Unit)?
) : PhoneNumberFormattingTextWatcher() {

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        super.onTextChanged(s, start, before, count)

        val cleanedPhoneNumber = s.toString()
            .replace(
                oldValue = " ",
                newValue = ""
            )
            .replace(
                oldValue = "(",
                newValue = ""
            )
            .replace(
                oldValue = ")",
                newValue = ""
            )
            .replace(
                oldValue = "-",
                newValue = ""
            )

        if (previousCleanedPhoneNumber != cleanedPhoneNumber) {
            previousCleanedPhoneNumber = cleanedPhoneNumber
            onEnteredCleanedPhoneNumber?.invoke(cleanedPhoneNumber)
        }
    }

    private var previousCleanedPhoneNumber: String? = null

}