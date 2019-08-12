package com.anvipo.angram.layers.core.base.classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.MyProgressDialog
import com.anvipo.angram.layers.core.base.CoreConstants.PROGRESS_TAG
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.core.dialogFragment.ItemsDialogFragment
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.showSnackbarMessage
import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter
import com.anvipo.angram.layers.presentation.common.mvp.MvpAppCompatFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException

@Suppress("unused")
abstract class BaseFragment :
    MvpAppCompatFragment(),
    BaseView,
    HasLogger,
    CoroutineScope by MainScope() {

    final override val className: String = this::class.java.name

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    final override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        extractDataFromBundle()
        setupClickListeners()
        setupToolbar()
        setupUI()

        if (savedInstanceState == null) {
            presenter.coldStart()
        } else {
            presenter.hotStart()
        }
    }

    final override fun onStart() {
        super.onStart()
        presenter.onStartTriggered()
    }

    final override fun onResume() {
        super.onResume()
        instanceStateSaved = false
        presenter.onResumeTriggered()
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
    }

    override fun onDestroy() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")
        cancel(cancellationException)
        super.onDestroy()
    }

    final override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }


    final override fun showItemsDialog(
        title: String?,
        items: List<String>,
        tag: String?,
        cancelable: Boolean
    ) {
        ItemsDialogFragment
            .create(
                title,
                items,
                tag,
                cancelable
            )
            .show(childFragmentManager, null)
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
            .show(childFragmentManager, null)
    }

    final override fun showErrorAlert(text: String) {
        showAlertMessage(title = getString(R.string.error_title), text = text)
    }

    final override fun showToastMessage(text: String) {
        Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
    }

    final override fun showSnackMessage(
        text: String,
        duration: Int
    ) {
        this.view?.showSnackbarMessage(
            text = text,
            duration = duration
        )
    }

    final override fun showProgress() {
        if (!isAdded || instanceStateSaved) return

        val fragment = childFragmentManager.findFragmentByTag(PROGRESS_TAG)

        if (fragment != null) {
            return
        }

        val myProgressDialog = MyProgressDialog()

        myProgressDialog.isCancelable = true
        myProgressDialog.show(childFragmentManager, PROGRESS_TAG)
        childFragmentManager.executePendingTransactions()

        myProgressDialog.dialog.setOnCancelListener {
            myProgressDialog.dismissAllowingStateLoss()
            presenter.onCanceledProgressDialog()
        }
    }

    final override fun hideProgress() {
        if (!isAdded || instanceStateSaved) return

        val myProgressDialog = childFragmentManager.findFragmentByTag(PROGRESS_TAG) ?: return

        (myProgressDialog as MyProgressDialog).dismissAllowingStateLoss()
        childFragmentManager.executePendingTransactions()
    }


    fun onBackPressed() {
        presenter.onBackPressed()
    }


    protected fun goToSettings() {
        val applicationID = BuildConfig.APPLICATION_ID //activity?.packageName is same

        val showApplicationDetailsSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationID, null)
        )

        startActivityForResult(showApplicationDetailsSettings, fromApplicationSettingsRequestCode)
    }

    protected open fun setupUI(): Unit = Unit
    protected open fun setupClickListeners(): Unit = Unit

    protected open fun extractDataFromBundle(): Unit = Unit

    protected open val shouldShowBackButton: Boolean
        get() = arguments?.getBoolean(ARG_SHOULD_SHOW_BACK_BUTTON) ?: false

    protected open val actionBarSubtitle: String = ""

    protected abstract val presenter: BasePresenter
    protected abstract val actionBarTitle: String
    protected abstract val actionBar: Toolbar
    protected abstract val layoutRes: Int
        @LayoutRes
        get

    @Suppress("MemberVisibilityCanBePrivate")
    protected val appCompatActivity: AppCompatActivity?
        get() = (activity as? AppCompatActivity)

    @Suppress("MemberVisibilityCanBePrivate")
    protected val supportActionBar: ActionBar?
        get() = appCompatActivity?.supportActionBar

    private var instanceStateSaved: Boolean = false

    private fun showBackButtonHelper() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        val supportActionBar = this.supportActionBar

        if (supportActionBar == null) {
            myLog(
                invokationPlace = invokationPlace,
                text = "supportActionBar = $supportActionBar"
            )
            return
        }

        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupToolbar() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        val appCompatActivity = this.appCompatActivity

        if (appCompatActivity == null) {
            myLog(
                invokationPlace = invokationPlace,
                text = "appCompatActivity = $appCompatActivity"
            )
            return
        }

        appCompatActivity.setSupportActionBar(actionBar)

        val supportActionBar = this.supportActionBar

        if (supportActionBar == null) {
            myLog(
                invokationPlace = invokationPlace,
                text = "supportActionBar = $supportActionBar"
            )
            return
        }

        if (shouldShowBackButton) {
            showBackButtonHelper()
        }

        supportActionBar.title = actionBarTitle
        supportActionBar.subtitle = actionBarSubtitle

        actionBar.setNavigationOnClickListener {
            appCompatActivity.onBackPressed()
        }
    }

    companion object {

        const val fromApplicationSettingsRequestCode: Int = 10_000

        const val ARG_SHOULD_SHOW_BACK_BUTTON: String = "arg_should_show_back_button"

    }

}