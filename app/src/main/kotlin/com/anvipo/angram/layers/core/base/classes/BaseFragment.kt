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
import com.anvipo.angram.layers.core.CoreHelpers.debugLog
import com.anvipo.angram.layers.core.MyProgressDialog
import com.anvipo.angram.layers.core.base.CoreConstants.PROGRESS_TAG
import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.anvipo.angram.layers.core.dialogFragment.ItemsDialogFragment
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.showSnackbarMessage
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
        setupUI()

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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }


    override fun showItemsDialog(
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
            .show(childFragmentManager, null)
    }

    override fun showErrorAlert(text: String) {
        showAlertMessage(title = getString(R.string.error_title), text = text)
    }

    override fun showToastMessage(text: String) {
        Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
    }

    override fun showSnackMessage(
        text: String,
        duration: Int
    ) {
        this.view?.showSnackbarMessage(
            text = text,
            duration = duration
        )
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
        val supportActionBar = this.supportActionBar

        if (supportActionBar == null) {
            debugLog("supportActionBar == null")
            return
        }

        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupToolbar() {
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

    companion object {

        const val fromApplicationSettingsRequestCode: Int = 10_000

        const val ARG_SHOULD_SHOW_BACK_BUTTON: String = "arg_should_show_back_button"

    }

}