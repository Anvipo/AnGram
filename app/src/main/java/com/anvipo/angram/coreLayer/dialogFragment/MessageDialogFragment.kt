package com.anvipo.angram.coreLayer.dialogFragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.anvipo.angram.coreLayer.argument

class MessageDialogFragment : DialogFragment() {

    private val startTag: String by argument(
        ARG_TAG,
        ""
    )
    private val title: String? by argument(ARG_TITLE)
    private val message: String? by argument(ARG_MESSAGE)
    private val positiveText: String? by argument(ARG_POSITIVE_TEXT)
    private val negativeText: String? by argument(ARG_NEGATIVE_TEXT)
    private val neutralText: String? by argument(ARG_NEUTRAL_TEXT)
    private val cancelable: Boolean by argument(ARG_CANCELABLE)

    private val clickListener: OnClickListener
        get() = when {
            parentFragment is OnClickListener -> parentFragment as OnClickListener
            activity is OnClickListener -> activity as OnClickListener
            else -> object : OnClickListener {}
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        this.isCancelable = cancelable

        return AlertDialog.Builder(context!!).apply {
            setTitle(title)
            setCancelable(cancelable)
            setMessage(message)
            positiveText?.let { positiveText ->
                setPositiveButton(positiveText) { _, _ ->
                    dismissAllowingStateLoss()
                    clickListener.dialogPositiveClicked(startTag)
                }
            }
            neutralText?.let { neutralText ->
                setNeutralButton(neutralText) { _, _ ->
                    dismissAllowingStateLoss()
                    clickListener.dialogNeutralClicked(startTag)
                }
            }
            negativeText?.let { negativeText ->
                setNegativeButton(negativeText) { _, _ ->
                    dismissAllowingStateLoss()
                    clickListener.dialogNegativeClicked(startTag)
                }
            }
        }.create()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        clickListener.dialogCanceled(startTag)
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_TAG = "arg_tag"
        private const val ARG_POSITIVE_TEXT = "arg_positive_text"
        private const val ARG_NEGATIVE_TEXT = "arg_negative_text"
        private const val ARG_NEUTRAL_TEXT = "arg_neutral_text"
        private const val ARG_CANCELABLE = "arg_cancelable"

        fun create(
            title: String? = null,
            message: String,
            positive: String? = null,
            negative: String? = null,
            neutral: String? = null,
            tag: String? = null,
            cancelable: Boolean = false
        ): MessageDialogFragment =
            MessageDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_POSITIVE_TEXT, positive)
                    putString(ARG_NEGATIVE_TEXT, negative)
                    putString(ARG_NEUTRAL_TEXT, neutral)
                    putBoolean(ARG_CANCELABLE, cancelable)
                    putString(ARG_TAG, tag)
                }
            }
    }

    interface OnClickListener {
        fun dialogPositiveClicked(tag: String) {}
        fun dialogNegativeClicked(tag: String) {}
        fun dialogNeutralClicked(tag: String) {}
        fun dialogCanceled(tag: String) {}
    }

}
