package com.anvipo.angram.presentationLayer.common.interfaces

import android.content.Context

interface Presentable {

    var onBackPressed: (() -> Unit)?

    val thisContext: Context?

}