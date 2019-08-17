package com.anvipo.angram.layers.core.events.parameters

class ShowErrorEventParameters(
    val text: String,
    val cancelable: Boolean,
    val messageDialogTag: String?
)