package com.anvipo.angram.layers.core.base.classes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import com.anvipo.angram.layers.core.showSnackbarMessage

@Suppress("unused")
abstract class BaseActivity :
    AppCompatActivity(),
    HasLogger {

    final override val className: String = this::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        setupToolbar()
        setupClickListeners()

        viewModel.onCreateTriggered()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStartTriggered()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        viewModel.onResumeTriggered()
    }

    final override fun onPause() {
        viewModel.onPauseTriggered()
        super.onPause()
    }


    protected open fun setupClickListeners(): Unit = Unit
    protected open fun setupToolbar(): Unit = Unit

    protected val currentFragment: BaseFragment
        get() = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment


    protected abstract val viewModel: BaseViewModel
    protected abstract val layoutRes: Int
        @LayoutRes
        get

    protected abstract val rootView: View

    private fun showAlertMessage(
        text: String,
        title: String?,
        cancelable: Boolean,
        messageDialogTag: String
    ) {
        MessageDialogFragment
            .create(
                message = text,
                title = title,
                positive = getString(android.R.string.ok),
                cancelable = cancelable,
                messageDialogTag = messageDialogTag
            )
            .show(supportFragmentManager, null)

    }

    private fun showErrorAlert(
        title: String? = getString(R.string.error_title),
        text: String,
        cancelable: Boolean = true,
        messageDialogTag: String = ""
    ) {
        showAlertMessage(
            title = title,
            text = text,
            cancelable = cancelable,
            messageDialogTag = messageDialogTag
        )
    }

    private fun showToastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun showSnackMessage(
        text: String,
        duration: Int
    ) {
        rootView.showSnackbarMessage(
            text = text,
            duration = duration
        )
    }

    private fun showConnectionStateSnackMessage(
        text: String,
        duration: Int
    ) {
        showSnackMessage(text, duration)
    }

}