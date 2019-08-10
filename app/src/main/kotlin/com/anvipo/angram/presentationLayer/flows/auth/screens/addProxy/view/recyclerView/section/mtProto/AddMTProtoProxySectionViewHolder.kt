package com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.section.mtProto

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.row.mtProto.AddMTProtoProxyRowListAdapter
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.view.recyclerView.section.base.AddProxySectionViewHolder
import kotlinx.android.synthetic.main.base_footer_view.view.*
import kotlinx.android.synthetic.main.base_header_view.view.*
import kotlinx.android.synthetic.main.section_add_proxy.view.*


class AddMTProtoProxySectionViewHolder(
    itemView: View
) : AddProxySectionViewHolder<AddMTProtoProxySection>(itemView) {

    override fun bind(section: AddMTProtoProxySection) {
        section.headerData?.title?.let {
            headerTextView.text = it
            headerView.visibility = View.VISIBLE
        }

        section.footerData?.title?.let {
            footerTextView.text = it
            footerView.visibility = View.VISIBLE
        }

        adapter.submitList(section.items)
    }

    private var headerView: View = itemView.add_proxy_header_view
    private var headerTextView: TextView = headerView.base_header_title_view

    private var recyclerView: RecyclerView = itemView.add_proxy_recycler_view

    private var footerView: View = itemView.add_proxy_footer_view
    private var footerTextView: TextView = footerView.base_footer_title_view

    private val adapter by lazy { AddMTProtoProxyRowListAdapter() }

    init {
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(itemView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

}
