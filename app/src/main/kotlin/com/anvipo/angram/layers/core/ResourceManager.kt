package com.anvipo.angram.layers.core

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.core.content.getSystemService
import com.anvipo.angram.R

@Suppress("unused")
class ResourceManager(val context: Context) {

    fun getString(@StringRes id: Int): String = context.getString(id)

    fun getString(
        @StringRes id: Int,
        vararg formatArgs: Any
    ): String = String.format(context.getString(id, *formatArgs))

    inline fun <reified T : Any> getSystemService(): T? = context.getSystemService()

    fun getSharedPreferences(): SharedPreferences {
        val preferenceFileKey = getString(R.string.preference_file_key)

        return context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE)
    }

}
