package com.anvipo.angram.coreLayer.base.baseClasses

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.coreLayer.mvp.MvpAppCompatActivity
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter

@Suppress("unused")
abstract class BaseActivity : MvpAppCompatActivity() {

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

}