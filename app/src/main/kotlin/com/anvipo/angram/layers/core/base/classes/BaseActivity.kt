package com.anvipo.angram.layers.core.base.classes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.ShowSnackMessageEventParameters
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowToastMessageEventParameters
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
        setupViewModelsObservers()

        if (savedInstanceState == null) {
            viewModel.onColdStart()
        } else {
            viewModel.onHotStart(savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStartTriggered()
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

    protected open fun setupViewModelsObservers() {
        viewModel
            .showErrorEvents
            .observe(this) {
                showErrorAlert(it)
            }

        viewModel
            .showAlertMessageEvents
            .observe(this) {
                showAlertMessage(it)
            }

        viewModel
            .showToastMessageEvents
            .observe(this) {
                showToastMessage(it)
            }

        viewModel
            .showSnackMessageEvents
            .observe(this) {
                showSnackMessage(it)
            }

        viewModel
            .showConnectionSnackMessageEvents
            .observe(this) {
                showSnackMessage(it)
            }
    }


    private fun showAlertMessage(
        showAlertMessageEvent: ShowAlertMessageEventParameters
    ) {
        MessageDialogFragment
            .create(
                message = showAlertMessageEvent.text,
                title = showAlertMessageEvent.title,
                positive = getString(android.R.string.ok),
                cancelable = showAlertMessageEvent.cancelable,
                messageDialogTag = showAlertMessageEvent.messageDialogTag
            )
            .show(supportFragmentManager, null)

    }

    private fun showErrorAlert(
        showErrorEventParameters: ShowErrorEventParameters
    ) {
        showAlertMessage(
            ShowAlertMessageEventParameters(
                title = getString(R.string.error_title),
                text = showErrorEventParameters.text,
                cancelable = showErrorEventParameters.cancelable,
                messageDialogTag = showErrorEventParameters.messageDialogTag
            )
        )
    }

    private fun showToastMessage(
        showToastMessageEventParameters: ShowToastMessageEventParameters
    ) {
        Toast.makeText(
            this,
            showToastMessageEventParameters.text,
            showToastMessageEventParameters.length
        ).show()
    }

    private fun showSnackMessage(
        showSnackMessageEventParameters: ShowSnackMessageEventParameters
    ) {
        rootView.showSnackbarMessage(
            text = showSnackMessageEventParameters.text,
            duration = showSnackMessageEventParameters.duration
        )
    }

}