package com.anvipo.angram.layers.core.events.parameters

class ShowAlertMessageEventParameters(
    val title: String?,
    val text: String,
    val cancelable: Boolean,
    val messageDialogTag: String?
)