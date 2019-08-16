package com.anvipo.angram.layers.core.events

class ShowAlertMessageEvent(
    val text: String,
    val title: String?,
    val cancelable: Boolean,
    val messageDialogTag: String
)