package com.anvipo.angram.layers.core

import com.google.android.material.snackbar.Snackbar

class ShowSnackMessageEventParameters(
    val text: String,
    val duration: Int = Snackbar.LENGTH_LONG
)