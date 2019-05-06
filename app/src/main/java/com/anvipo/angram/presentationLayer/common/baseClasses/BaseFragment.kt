package com.anvipo.angram.presentationLayer.common.baseClasses

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.coreLayer.mvp.MvpAppCompatFragment
import com.anvipo.angram.presentationLayer.common.interfaces.BaseView
import com.anvipo.angram.presentationLayer.common.interfaces.IBasePresenter

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    protected val fragmentContext: Context?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context
        } else {
            activity
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            print("")
        }

        setupClickListeners()
        setupToolbar()
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
        supportActionBar.subtitle = actionBarSubitle

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


    internal open fun onBackPressed() {}

    protected abstract fun setupClickListeners()

    protected abstract val presenter: IBasePresenter

    protected abstract val actionBarTitle: String
    protected abstract val actionBarSubitle: String
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

}