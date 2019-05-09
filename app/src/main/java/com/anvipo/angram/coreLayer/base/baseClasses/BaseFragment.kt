package com.anvipo.angram.coreLayer.base.baseClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.coreLayer.MessageDialogFragment
import com.anvipo.angram.coreLayer.MyProgressDialog
import com.anvipo.angram.coreLayer.base.CoreConstants.PROGRESS_TAG
import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.anvipo.angram.coreLayer.showSnackbarMessage
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.common.mvp.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupClickListeners()
        setupToolbar()

        if (savedInstanceState == null) {
            presenter.coldStart()
        } else {
            presenter.hotStart()
        }
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
    }


    override fun showToastMessage(text: String) {
        Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
    }

    override fun showSnackMessage(text: String) {
        this.view?.showSnackbarMessage(text)
    }

    override fun showAlertMessage(text: String) {
        MessageDialogFragment
            .create(message = text)
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
            Log.w(App.TAG, "appCompatActivity == null")
            return
        }

        appCompatActivity.setSupportActionBar(actionBar)

        val supportActionBar = this.supportActionBar

        if (supportActionBar == null) {
            Log.d(App.TAG, "supportActionBar == null")
            return
        }

        supportActionBar.title = actionBarTitle
        supportActionBar.subtitle = actionBarSubtitle

        actionBar.setNavigationOnClickListener {
            appCompatActivity.onBackPressed()
        }
    }

    protected fun showBackButton() {
        val supportActionBar = this.supportActionBar

        if (supportActionBar == null) {
            Log.d(App.TAG, "supportActionBar == null")
            return
        }

        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
    }

    protected abstract fun setupClickListeners()

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

    private var instanceStateSaved: Boolean = false

}