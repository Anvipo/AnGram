package com.anvipo.angram.layers.core.dialogFragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.anvipo.angram.layers.core.argument

class MessageDialogFragment : DialogFragment() {

    private val messageDialogTag: String? by argument(ARG_MESSAGE_DIALOG_TAG)
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
                    clickListener.messageDialogPositiveClicked(messageDialogTag)
                }
            }
            neutralText?.let { neutralText ->
                setNeutralButton(neutralText) { _, _ ->
                    dismissAllowingStateLoss()
                    clickListener.messageDialogNeutralClicked(messageDialogTag)
                }
            }
            negativeText?.let { negativeText ->
                setNegativeButton(negativeText) { _, _ ->
                    dismissAllowingStateLoss()
                    clickListener.messageDialogNegativeClicked(messageDialogTag)
                }
            }
        }.create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        clickListener.messageDialogCanceled(messageDialogTag)
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_MESSAGE_DIALOG_TAG = "arg_message_dialog_tag"
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
            messageDialogTag: String? = null,
            cancelable: Boolean = true
        ): MessageDialogFragment =
            MessageDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_POSITIVE_TEXT, positive)
                    putString(ARG_NEGATIVE_TEXT, negative)
                    putString(ARG_NEUTRAL_TEXT, neutral)
                    putBoolean(ARG_CANCELABLE, cancelable)
                    putString(ARG_MESSAGE_DIALOG_TAG, messageDialogTag)
                }
            }
    }

    interface OnClickListener {

        fun messageDialogPositiveClicked(tag: String?): Unit = Unit
        fun messageDialogNegativeClicked(tag: String?): Unit = Unit
        fun messageDialogNeutralClicked(tag: String?): Unit = Unit
        fun messageDialogCanceled(tag: String?): Unit = Unit

    }

}
