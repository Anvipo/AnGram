package com.anvipo.angram.presentationLayer.common.baseClasses.view.section

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.anvipo.angram.presentationLayer.common.interfaces.view.section.BaseSection

@Suppress("unused")
abstract class SectionListAdapter<BS : BaseSection, SVH : SectionViewHolder<BS>> :
    ListAdapter<BS, SVH> {

    constructor(
        diffCallback: DiffUtil.ItemCallback<BS>
    ) : super(diffCallback)

    constructor(
        config: AsyncDifferConfig<BS>
    ) : super(config)

    final override fun getItemViewType(position: Int): Int = getItem(position).viewType

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SVH {
        val inflater = LayoutInflater.from(parent.context)

        val viewData = getItem(0).viewsData.find { it.viewType == viewType }!!

        val itemView = inflater.inflate(viewData.layoutRes, parent, false)

        return viewData.createSectionViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: SVH,
        position: Int
    ): Unit = holder.bind(section = getItem(position))

}
