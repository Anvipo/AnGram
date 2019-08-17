package com.anvipo.angram.layers.core.base.classes.recyclerView.row

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.row.BaseRow

@Suppress("unused")
abstract class RowListAdapter<BR : BaseRow, RVH : RowViewHolder<BR>> :
    ListAdapter<BR, RVH> {

    constructor(
        diffCallback: DiffUtil.ItemCallback<BR>
    ) : super(diffCallback)

    constructor(
        config: AsyncDifferConfig<BR>
    ) : super(config)

    final override fun getItemViewType(position: Int): Int = getItem(position).viewType

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVH {
        val inflater = LayoutInflater.from(parent.context)

        val viewData = getItem(0).viewsData.find { it.viewType == viewType }!!

        val itemView = inflater.inflate(viewData.layoutRes, parent, false)

        val rowViewHolder = viewData.createRowViewHolder<BR, RVH>(itemView)
        rowViewHolder.onCreate()
        return rowViewHolder
    }

    final override fun onBindViewHolder(
        holder: RVH,
        position: Int
    ): Unit = holder.bind(row = getItem(position))

    override fun onViewAttachedToWindow(holder: RVH) {
        super.onViewAttachedToWindow(holder)
        holder.onStart()
        holder.onResume()
    }

    override fun onViewDetachedFromWindow(holder: RVH) {
        super.onViewDetachedFromWindow(holder)
        holder.onDestroy()
    }

}