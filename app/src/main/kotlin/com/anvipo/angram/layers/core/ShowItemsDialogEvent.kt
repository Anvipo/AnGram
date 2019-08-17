package com.anvipo.angram.layers.core

class ShowItemsDialogEvent(
    val title: String?,
    val items: List<String>,
    val tag: String?,
    val cancelable: Boolean
)