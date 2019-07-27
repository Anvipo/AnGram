package com.anvipo.angram.coreLayer.base.baseClasses

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
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.MessageDialogFragment
import com.anvipo.angram.coreLayer.MyProgressDialog
import com.anvipo.angram.coreLayer.base.CoreConstants.PROGRESS_TAG
import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.anvipo.angram.coreLayer.showSnackbarMessage
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.common.mvp.MvpAppCompatFragment

@Suppress("unused")
abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        extractDataFromBundle()
        setupClickListeners()
        setupToolbar()

        if (savedInstanceState == null) {
            presenter.coldStart()
        } else {
            presenter.hotStart()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStartTriggered()
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
        presenter.onResumeTriggered()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
    }


    override fun showErrorAlert(text: String) {
        MessageDialogFragment
            .create(
                message = text,
                title = getString(R.string.error_title),
                positive = getString(android.R.string.ok)
            )
            .show(childFragmentManager, null)
    }

    override fun showToastMessage(text: String) {
        Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
    }

    override fun showSnackMessage(
        text: String,
        duration: Int,
        withProgressBar: Boolean,
        isProgressBarIndeterminate: Boolean
    ) {
        this.view?.showSnackbarMessage(
            text = text,
            duration = duration,
            withProgressBar = withProgressBar,
            isProgressBarIndeterminate = isProgressBarIndeterminate
        )
    }

    override fun showAlertMessage(text: String) {
        MessageDialogFragment
            .create(
                message = text,
                positive = getString(android.R.string.ok)
            )
            .show(childFragmentManager, null)
    }

    override fun showProgress() {
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

    override fun hideProgress() {
        if (!isAdded || instanceStateSaved) return

        val myProgressDialog = childFragmentManager.findFragmentByTag(PROGRESS_TAG) ?: return

        (myProgressDialog as MyProgressDialog).dismissAllowingStateLoss()
        childFragmentManager.executePendingTransactions()
    }


    internal fun onBackPressed() {
        presenter.onBackPressed()
    }


    protected open fun setupToolbar() {
        val appCompatActivity = this.appCompatActivity

        if (appCompatActivity == null) {
            debugLog("appCompatActivity == null")
            return
        }

        appCompatActivity.setSupportActionBar(actionBar)

        val supportActionBar = this.supportActionBar

        if (supportActionBar == null) {
            debugLog("supportActionBar == null")
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

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun showBackButtonHelper() {
        val supportActionBar = this.supportActionBar

        if (supportActionBar == null) {
            debugLog("supportActionBar == null")
            return
        }

        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
    }

    protected fun goToSettings() {
        val applicationID = BuildConfig.APPLICATION_ID //activity?.packageName is same

        val showApplicationDetailsSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationID, null)
        )

        startActivityForResult(showApplicationDetailsSettings, fromApplicationSettingsRequestCode)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    protected abstract fun setupClickListeners()

    protected open fun extractDataFromBundle() {}

    protected abstract val presenter: BasePresenter

    protected abstract val actionBarTitle: String
    protected open val actionBarSubtitle: String = ""
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

    protected var shouldShowBackButton: Boolean = false

    private var instanceStateSaved: Boolean = false

    companion object {

        internal const val fromApplicationSettingsRequestCode: Int = 10_000

    }

}