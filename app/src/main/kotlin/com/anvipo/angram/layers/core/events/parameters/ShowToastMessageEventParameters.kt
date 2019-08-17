package com.anvipo.angram.layers.core.events.parameters

import android.widget.Toast

class ShowToastMessageEventParameters(
    val text: String,
    val length: Int = Toast.LENGTH_LONG
)