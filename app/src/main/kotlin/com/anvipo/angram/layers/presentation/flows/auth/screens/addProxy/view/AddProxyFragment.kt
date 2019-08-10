package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.IndexPath
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.hideWithAnimate
import com.anvipo.angram.layers.core.showWithAnimate
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule.addProxyPresenterQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.presenter.AddProxyPresenter
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.presenter.AddProxyPresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.headersAndFooters.mtProto.AddMTProtoProxyHeaderData
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.row.mtProto.AddMTProtoProxyRow
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section.AddProxySectionListAdapter
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.recyclerView.section.mtProto.AddMTProtoProxySection
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_proxy.*
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.get

class AddProxyFragment :
    BaseFragment(),
    AddProxyView,
    MessageDialogFragment.OnClickListener {

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

    override fun setupUI() {
        add_proxy_rv.layoutManager = LinearLayoutManager(this.context)
        add_proxy_rv.adapter = adapter
        add_proxy_rv.setHasFixedSize(true)
    }

    override fun setupClickListeners() {
        add_proxy_button.setOnClickListener(::onAddProxyButtonClicked)
    }

    override fun showAddProxyButton() {
        add_proxy_button.showWithAnimate()
    }

    override fun hideAddProxyButton() {
        add_proxy_button.hideWithAnimate()
    }

    override fun messageDialogPositiveClicked(tag: String) {
        presenter.messageDialogPositiveClicked(tag)
    }

    override val presenter: AddProxyPresenter by lazy { mPresenter }

    override val actionBarTitle: String by lazy { getString(R.string.enter_proxy_server_data) }

    override val actionBar: Toolbar
        get() = add_proxy_toolbar as Toolbar

    override val layoutRes: Int by lazy { R.layout.fragment_add_proxy }

    @ProvidePresenter
    fun providePresenter(): AddProxyPresenterImp =
        get(addProxyPresenterQualifier)

    @InjectPresenter
    lateinit var mPresenter: AddProxyPresenterImp

    private val adapter by lazy { AddProxySectionListAdapter() }

    @Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")
    private fun setupMTProtoUI() {
        presenter.setProxyType(TdApi.ProxyTypeMtproto())

        adapter.submitList(
            listOf(
                createFirstSection(),
                createSecondSection()
            )
        )
    }

    @Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")
    private fun createFirstSection(): AddMTProtoProxySection {
        val firstSectionIndex = 0u

        return AddMTProtoProxySection(
            index = firstSectionIndex,
            headerData = AddMTProtoProxyHeaderData(
                title = getString(R.string.connect_add_mt_proto_proxy_label)
            ),
            items = listOf(
                AddMTProtoProxyRow(
                    textInputHint = getString(R.string.server_add_mt_proto_proxy_label),
                    indexPath = IndexPath(section = firstSectionIndex, row = 0u),
                    onTextChanged = { presenter.onServerTextChanged(it) }
                ),
                AddMTProtoProxyRow(
                    textInputHint = getString(R.string.port_add_mt_proto_proxy_label),
                    indexPath = IndexPath(section = firstSectionIndex, row = 1u),
                    onTextChanged = { presenter.onPortTextChanged(it) },
                    textInputType = InputType.TYPE_CLASS_NUMBER
                )
            )
        )
    }

    @Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")
    private fun createSecondSection(): AddMTProtoProxySection {
        val secondSectionIndex = 1u

        return AddMTProtoProxySection(
            index = secondSectionIndex,
            headerData = AddMTProtoProxyHeaderData(
                title = getString(R.string.credentials_add_mtproto_proxy_label)
            ),
            items = listOf(
                AddMTProtoProxyRow(
                    textInputHint = getString(R.string.key_add_mtproto_proxy_label),
                    indexPath = IndexPath(section = secondSectionIndex, row = 0u),
                    onTextChanged = { presenter.onSecretTextChanged(it) }
                )
            )
        )
    }

    private fun onAddProxyButtonClicked(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        presenter.addProxyButtonTapped()
    }

}
