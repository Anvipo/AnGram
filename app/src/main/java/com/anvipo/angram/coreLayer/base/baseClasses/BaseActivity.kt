package com.anvipo.angram.coreLayer.base.baseClasses

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.MessageDialogFragment
import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
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

    override fun showAlertMessage(text: String) {
        MessageDialogFragment
            .create(message = text)
            .show(supportFragmentManager, null)
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

    override fun showErrorAlert(text: String) {
        MessageDialogFragment
            .create(
                message = text,
                title = getString(R.string.error_title),
                positive = getString(android.R.string.ok)
            )
            .show(supportFragmentManager, null)
    }

    protected abstract fun setupClickListeners()
    protected open fun setupToolbar() {}

    protected abstract val presenter: BasePresenter

    protected open val actionBarTitle: String = ""
    protected open val actionBarSubtitle: String = ""
    protected open val actionBar: Toolbar
        get() = TODO()

    protected abstract val layoutRes: Int
        @LayoutRes
        get

    protected val simpleName: String
        get() = this::class.java.simpleName

    protected abstract val rootView: View

}