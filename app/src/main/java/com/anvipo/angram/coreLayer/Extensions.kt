package com.anvipo.angram.coreLayer

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.google.android.material.snackbar.Snackbar

fun debugLog(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(App.TAG, message)
    }
}

fun assertionFailure(message: String = "Debug error") {
    if (BuildConfig.DEBUG) {
        throw DebugError(message)
    }
}

fun Context.color(colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.getTintDrawable(
    drawableRes: Int,
    colorRes: Int
): Drawable {
    val source = ContextCompat.getDrawable(this, drawableRes)!!.mutate()
    val wrapped = DrawableCompat.wrap(source)
    DrawableCompat.setTint(wrapped, color(colorRes))
    return wrapped
}

fun Context.getTintDrawable(
    drawableRes: Int,
    colorResources: IntArray,
    states: Array<IntArray>
): Drawable {
    val source = ContextCompat.getDrawable(this, drawableRes)!!.mutate()
    val wrapped = DrawableCompat.wrap(source)
    DrawableCompat.setTintList(wrapped, ColorStateList(states, colorResources.map { color(it) }.toIntArray()))
    return wrapped
}

fun TextView.setStartDrawable(drawable: Drawable) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        drawable,
        null,
        null,
        null
    )
}

fun ImageView.tint(colorRes: Int) = this.setColorFilter(this.context.color(colorRes))

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun TextView.showTextOrHide(str: String?) {
    this.text = str
    this.visible(!str.isNullOrBlank())
}

fun Fragment.tryOpenLink(
    link: String,
    basePath: String? = "https://google.com/search?q="
) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                when {
                    URLUtil.isValidUrl(link) -> Uri.parse(link)
                    else -> Uri.parse(basePath + link)
                }
            )
        )
    } catch (e: Exception) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://google.com/search?q=$link")
            )
        )
    }
}

fun Fragment.shareText(text: String) {
    startActivity(
        Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            },
            getString(R.string.share_to)
        )
    )
}

fun Fragment.sendEmail(email: String) {
    startActivity(
        Intent.createChooser(
            Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null)),
            null
        )
    )
}

fun Fragment.showSnackMessage(message: String) {
    view?.let {
        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
        val messageTextView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        messageTextView.setTextColor(Color.WHITE)
        snackbar.show()
    }
}

fun AppCompatActivity.hideKeyboard() {
    currentFocus?.apply {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun View.setBackgroundTintByColor(@ColorInt color: Int) {
    val wrappedDrawable = DrawableCompat.wrap(background)
    DrawableCompat.setTint(wrappedDrawable.mutate(), color)
}

fun View.showSnackBar(
    message: String,
    duration: Int
) {
    Snackbar.make(this, message, duration).show()
}
