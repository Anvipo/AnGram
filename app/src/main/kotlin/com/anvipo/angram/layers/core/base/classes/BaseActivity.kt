package com.anvipo.angram.layers.core.base.classes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.showSnackbarMessage
import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter
import com.anvipo.angram.layers.presentation.common.mvp.MvpAppCompatActivity

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

    override fun showErrorAlert(text: String) {
        showAlertMessage(title = getString(R.string.error_title), text = text)
    }

    override fun showToastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun showSnackMessage(
        text: String,
        duration: Int
    ) {
        rootView.showSnackbarMessage(
            text = text,
            duration = duration
        )
    }

    override fun showConnectionStateSnackMessage(
        text: String,
        duration: Int
    ) {
        showSnackMessage(text, duration)
    }


    protected open fun setupClickListeners(): Unit = Unit
    protected open fun setupToolbar(): Unit = Unit

    protected val currentFragment: BaseFragment
        get() = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment


    protected abstract val presenter: BasePresenter
    protected abstract val layoutRes: Int
        @LayoutRes
        get

    protected abstract val rootView: View

}