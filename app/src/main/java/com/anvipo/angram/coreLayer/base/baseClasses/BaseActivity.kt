package com.anvipo.angram.coreLayer.base.baseClasses

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.anvipo.angram.coreLayer.dialogFragment.MessageDialogFragment
import com.anvipo.angram.coreLayer.showSnackbarMessage
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.common.mvp.MvpAppCompatActivity

@Suppress("unused")
abstract class BaseActivity : MvpAppCompatActivity(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        setupToolbar()
        setupClickListeners()

        if (savedInstanceState == null) {
            presenter.coldStart()
        } else {
            presenter.hotStart()
        }
    }

    override fun onPause() {
        presenter.onPauseTriggered()
        super.onPause()
    }

    override fun showAlertMessage(
        text: String,
        title: String?
    ) {
        MessageDialogFragment
            .create(
                message = text,
                title = title,
                positive = getString(android.R.string.ok)
            )
            .show(supportFragmentManager, null)
    }

    override fun showErrorAlert(text: String) {
        showAlertMessage(title = getString(R.string.error_title), text = text)
    }

    override fun showToastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun showSnackMessage(
        text: String,
        duration: Int,
        withProgressBar: Boolean,
        isProgressBarIndeterminate: Boolean
    ) {
        rootView.showSnackbarMessage(
            text = text,
            duration = duration,
            withProgressBar = withProgressBar,
            isProgressBarIndeterminate = isProgressBarIndeterminate
        )
    }

    override fun showConnectionErrorSnackMessage(
        text: String,
        duration: Int,
        withProgressBar: Boolean,
        isProgressBarIndeterminate: Boolean
    ) {
        showSnackMessage(text, duration, withProgressBar, isProgressBarIndeterminate)
    }


    protected open fun setupClickListeners(): Unit = Unit
    protected open fun setupToolbar(): Unit = Unit

    protected abstract val presenter: BasePresenter
    protected abstract val layoutRes: Int
        @LayoutRes
        get

    protected abstract val rootView: View

}