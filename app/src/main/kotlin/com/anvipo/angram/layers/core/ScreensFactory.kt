package com.anvipo.angram.layers.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.terrakok.cicerone.android.support.SupportAppScreen

@Suppress("unused")
object ScreensFactory {

    data class ExternalBrowserFlow(
        val uri: Uri
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent =
            Intent(Intent.ACTION_VIEW, uri)
    }

    data class ShareFlow(
        val text: String
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent =
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                },
                text
            )!!
    }


}