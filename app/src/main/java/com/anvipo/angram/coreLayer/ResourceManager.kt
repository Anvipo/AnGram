package com.anvipo.angram.coreLayer

import android.content.Context

class ResourceManager(private val context: Context) {

    fun getString(id: Int): String = context.getString(id)

    fun getString(
        id: Int,
        vararg formatArgs: Any
    ): String = String.format(context.getString(id, *formatArgs))

}
