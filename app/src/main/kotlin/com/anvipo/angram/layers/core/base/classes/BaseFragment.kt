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
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.CoreConstants.PROGRESS_TAG
import com.anvipo.angram.layers.core.ShowItemsDialogEventParameters
import com.anvipo.angram.layers.core.ShowSnackMessageEventParameters
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.dialogFragment.ItemsDialogFragment
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowToastMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.core.logHelpers.HasLogger
import com.anvipo.angram.layers.core.showSnackbarMessage
import com.anvipo.angram.layers.core.views.MyProgressDialog

@Suppress("unused")
abstract class BaseFragment :
    Fragment(),
    HasLogger {

    final override val className: String by lazy { this::class.java.name }

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
        setupViewModelsObservers()

        viewModel.onCreateTriggered()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStartTriggered()
    }

    final override fun onResume() {
        super.onResume()
        instanceStateSaved = false
        viewModel.onResumeTriggered()
    }

    final override fun onPause() {
        viewModel.onPauseTriggered()
        super.onPause()
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
    }

    final override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    fun onBackPressed() {
        viewModel.onBackPressed()
    }


    protected open val shouldShowBackButton: Boolean
        get() = arguments?.getBoolean(ARG_SHOULD_SHOW_BACK_BUTTON) ?: false

    protected open val actionBarSubtitle: String = ""

    protected abstract val viewModel: BaseViewModel

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

    protected open fun setupUI(): Unit = Unit
    protected open fun setupClickListeners(): Unit = Unit
    protected open fun setupViewModelsObservers() {
        viewModel
            .showErrorEvents
            .observe(this) {
                showErrorAlert(it)
            }

        viewModel
            .showViewEvents
            .observe(this) {
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (it) {
                    SHOW -> showProgress()
                    HIDE -> hideProgress()
                }
            }

        viewModel
            .showAlertMessageEvents
            .observe(this) {
                showAlertMessage(it)
            }

        viewModel
            .showItemsDialogEvents
            .observe(this) {
                showItemsDialog(it)
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
    }

    protected open fun extractDataFromBundle(): Unit = Unit

    protected fun goToSettings() {
        val applicationID = BuildConfig.APPLICATION_ID //activity?.packageName is same

        val showApplicationDetailsSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationID, null)
        )

        startActivityForResult(showApplicationDetailsSettings, fromApplicationSettingsRequestCode)
    }

    private var instanceStateSaved: Boolean = false

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

    private fun showProgress() {
        if (!isAdded || instanceStateSaved) return

        val fragment = childFragmentManager.findFragmentByTag(PROGRESS_TAG)

        if (fragment != null) {
            return
        }

        val myProgressDialog = MyProgressDialog()

        myProgressDialog.isCancelable = true
        myProgressDialog.show(childFragmentManager, PROGRESS_TAG)
        childFragmentManager.executePendingTransactions()

        myProgressDialog.dialog?.setOnCancelListener {
            myProgressDialog.dismissAllowingStateLoss()
            viewModel.onCanceledProgressDialog()
        }
    }

    private fun hideProgress() {
        if (!isAdded || instanceStateSaved) return

        val myProgressDialog = childFragmentManager.findFragmentByTag(PROGRESS_TAG) ?: return

        (myProgressDialog as MyProgressDialog).dismissAllowingStateLoss()
        childFragmentManager.executePendingTransactions()
    }

    private fun showItemsDialog(
        showItemsDialogEvent: ShowItemsDialogEventParameters
    ) {
        ItemsDialogFragment
            .create(
                showItemsDialogEvent.title,
                showItemsDialogEvent.items,
                showItemsDialogEvent.tag,
                showItemsDialogEvent.cancelable
            )
            .show(childFragmentManager, null)
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
            .show(childFragmentManager, null)
    }

    private fun showToastMessage(
        showToastMessageEventParameters: ShowToastMessageEventParameters
    ) {
        val context = this.context ?: return

        Toast.makeText(
            context,
            showToastMessageEventParameters.text,
            showToastMessageEventParameters.length
        ).show()
    }

    private fun showSnackMessage(
        showSnackMessageEventParameters: ShowSnackMessageEventParameters
    ) {
        this.view?.showSnackbarMessage(
            text = showSnackMessageEventParameters.text,
            duration = showSnackMessageEventParameters.duration
        )
    }

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