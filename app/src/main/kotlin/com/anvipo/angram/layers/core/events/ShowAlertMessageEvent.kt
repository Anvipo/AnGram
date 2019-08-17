package com.anvipo.angram.layers.core.events

class ShowAlertMessageEvent(
    val title: String?,
    val text: String,
    val cancelable: Boolean,
    val messageDialogTag: String
)