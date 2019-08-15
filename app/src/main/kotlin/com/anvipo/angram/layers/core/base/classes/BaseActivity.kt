package com.anvipo.angram.layers.core.base.classes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.UiScope
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.showSnackbarMessage
import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter
import com.anvipo.angram.layers.presentation.common.mvp.MvpAppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException

@Suppress("unused")
abstract class BaseActivity :
    MvpAppCompatActivity(),
    BaseView,
    HasLogger,
    CoroutineScope by UiScope() {

    final override val className: String = this::class.java.name

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

    final override fun onPause() {
        presenter.onPauseTriggered()
        super.onPause()
    }

    final override fun onDestroy() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")
        try {
            cancel(cancellationException)
        } catch (exception: Exception) {
            myLog(
                invokationPlace = methodName,
                text = "exception = $exception"
            )
        }
        super.onDestroy()
    }

    final override fun showAlertMessage(
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

    final override fun showErrorAlert(text: String) {
        showAlertMessage(title = getString(R.string.error_title), text = text)
    }

    final override fun showToastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    final override fun showSnackMessage(
        text: String,
        duration: Int
    ) {
        rootView.showSnackbarMessage(
            text = text,
            duration = duration
        )
    }

    final override fun showConnectionStateSnackMessage(
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