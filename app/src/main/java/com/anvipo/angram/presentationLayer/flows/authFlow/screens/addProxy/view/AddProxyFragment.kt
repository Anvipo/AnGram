package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.view

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.di.AddProxyModule.addProxyPresenterQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.presenter.AddProxyPresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.presenter.AddProxyPresenterImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.types.ProxyType
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_proxy.*
import org.koin.android.ext.android.get

class AddProxyFragment : BaseFragment(), AddProxyView {

    companion object {
        private const val ARG_PROXY_TYPE = "arg_proxy_type"

        @JvmStatic
        fun createNewInstance(
            proxyType: ProxyType,
            shouldShowBackButton: Boolean
        ): AddProxyView =
            AddProxyFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PROXY_TYPE, proxyType)
                    putBoolean(ARG_SHOULD_SHOW_BACK_BUTTON, shouldShowBackButton)
                }
            }
    }

    override fun extractDataFromBundle() {
        (arguments?.getSerializable(ARG_PROXY_TYPE) as ProxyType).let {
            when (it) {
                ProxyType.MTPROTO -> setupMTProtoUI()
            }
        }
    }

    override val presenter: AddProxyPresenter by lazy { mPresenter }

    override val actionBarTitle: String by lazy { "Введите данные прокси сервера" }

    override val actionBar: Toolbar
        get() = add_proxy_toolbar as Toolbar

    override val layoutRes: Int by lazy { R.layout.fragment_add_proxy }

    @Suppress("ProtectedInFinal")
    @ProvidePresenter
    protected fun providePresenter(): AddProxyPresenterImp =
        get(addProxyPresenterQualifier)

    @InjectPresenter
    internal lateinit var mPresenter: AddProxyPresenterImp

    private fun setupMTProtoUI() {
        // TODO
        println()
    }

}
