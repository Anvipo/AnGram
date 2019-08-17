package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.IndexPath
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.core.hideWithAnimate
import com.anvipo.angram.layers.core.showWithAnimate
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.di.AddProxyModule.addProxyViewModelQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.headersAndFooters.mtProto.AddMTProtoProxyHeaderData
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.row.mtProto.AddMTProtoProxyRow
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.section.AddProxySectionListAdapter
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.recyclerView.section.mtProto.AddMTProtoProxySection
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModelImpl
import kotlinx.android.synthetic.main.fragment_add_proxy.*
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.get

class AddProxyFragment :
    BaseFragment(),
    MessageDialogFragment.OnClickListener {

    companion object {
        fun createNewInstance(
            proxyType: ProxyType,
            shouldShowBackButton: Boolean
        ): AddProxyFragment =
            AddProxyFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PROXY_TYPE, proxyType)
                    putBoolean(ARG_SHOULD_SHOW_BACK_BUTTON, shouldShowBackButton)
                }
            }

        private const val ARG_PROXY_TYPE = "arg_proxy_type"
    }

    override val viewModel: AddProxyViewModel by viewModels<AddProxyViewModelImpl> {
        get(addProxyViewModelQualifier)
    }

    override val actionBarTitle: String
            by lazy { getString(R.string.enter_proxy_server_data) }

    override val actionBar: Toolbar
        get() = add_proxy_toolbar as Toolbar

    override val layoutRes: Int by lazy { R.layout.fragment_add_proxy }

    @ExperimentalUnsignedTypes
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

    override fun setupViewModelsObservers() {
        super.setupViewModelsObservers()
        viewModel
            .showAddProxyEvents
            .observe(this) {
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (it) {
                    SHOW -> showAddProxyButton()
                    HIDE -> hideAddProxyButton()
                }
            }
    }

    override fun setupClickListeners() {
        add_proxy_button.setOnClickListener(::onAddProxyButtonClicked)
    }

    override fun messageDialogPositiveClicked(tag: String?) {
        viewModel.messageDialogPositiveClicked(tag)
    }


    private val adapter by lazy { AddProxySectionListAdapter() }

    private fun showAddProxyButton() {
        add_proxy_button.showWithAnimate()
    }

    private fun hideAddProxyButton() {
        add_proxy_button.hideWithAnimate()
    }

    @ExperimentalUnsignedTypes
    private fun setupMTProtoUI() {
        viewModel.setProxyType(TdApi.ProxyTypeMtproto())

        adapter.submitList(
            listOf(
                createFirstSection(),
                createSecondSection()
            )
        )
    }

    @ExperimentalUnsignedTypes
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
                    onTextChanged = { viewModel.onServerTextChanged(it) }
                ),
                AddMTProtoProxyRow(
                    textInputHint = getString(R.string.port_add_mt_proto_proxy_label),
                    indexPath = IndexPath(section = firstSectionIndex, row = 1u),
                    onTextChanged = { viewModel.onPortTextChanged(it) },
                    textInputType = InputType.TYPE_CLASS_NUMBER
                )
            )
        )
    }

    @ExperimentalUnsignedTypes
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
                    onTextChanged = { viewModel.onSecretTextChanged(it) }
                )
            )
        )
    }

    private fun onAddProxyButtonClicked(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        viewModel.addProxyButtonTapped()
    }

}
