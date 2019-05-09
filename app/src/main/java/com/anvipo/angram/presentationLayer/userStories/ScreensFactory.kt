package com.anvipo.angram.presentationLayer.userStories

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.terrakok.cicerone.android.support.SupportAppScreen

object ScreensFactory {

    data class ExternalBrowserFlow(
        val url: String
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
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