package com.anvipo.angram.coreLayer.dialogFragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.anvipo.angram.coreLayer.argument
import java.util.*

class ItemsDialogFragment : DialogFragment() {

    private val startTag: String by argument(
        ARG_TAG,
        ""
    )
    private val title: String? by argument(ARG_TITLE)
    private val items: List<String>? by argument(ARG_ITEMS)
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
            setItems(
                items?.toTypedArray(),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    clickListener.itemClicked(startTag, which)
                }
            )
        }.create()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        clickListener.dialogCanceled(startTag)
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_ITEMS = "arg_items"
        private const val ARG_TAG = "arg_tag"
        private const val ARG_CANCELABLE = "arg_cancelable"

        fun create(
            title: String? = null,
            items: List<String>,
            tag: String? = null,
            cancelable: Boolean = false
        ): ItemsDialogFragment =
            ItemsDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putStringArrayList(ARG_ITEMS, items.toMutableList() as ArrayList<String>)
                    putBoolean(ARG_CANCELABLE, cancelable)
                    putString(ARG_TAG, tag)
                }
            }
    }

    interface OnClickListener {
        fun itemClicked(
            tag: String,
            index: Int
        ) {
        }

        fun dialogCanceled(tag: String) {}
    }

}
