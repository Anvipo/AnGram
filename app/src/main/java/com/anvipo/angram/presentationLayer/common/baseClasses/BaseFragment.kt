package com.anvipo.angram.presentationLayer.common.baseClasses

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable

abstract class BaseFragment : Fragment(), Presentable {

    override var onBackPressed: (() -> Unit)? = null

    override val thisContext: Context?
        get() = this.context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
            Log.w(App.TAG, "supportActionBar == null")
            return
        }

        if (onBackPressed != null) {
            supportActionBar.setDisplayShowHomeEnabled(true)
            supportActionBar.setDisplayHomeAsUpEnabled(true)
        }

        supportActionBar.title = actionBarTitle
        supportActionBar.subtitle = actionBarSubitle

        actionBar.setNavigationOnClickListener {
            appCompatActivity.onBackPressed()
        }
    }

    protected abstract fun setupClickListeners()

    protected abstract val actionBarTitle: String
    protected abstract val actionBarSubitle: String
    protected abstract val actionBar: Toolbar

    protected abstract val layoutRes: Int

    @Suppress("MemberVisibilityCanBePrivate")
    protected val appCompatActivity: AppCompatActivity?
        get() = (activity as? AppCompatActivity)

    @Suppress("MemberVisibilityCanBePrivate")
    protected val supportActionBar: ActionBar?
        get() = appCompatActivity?.supportActionBar

}