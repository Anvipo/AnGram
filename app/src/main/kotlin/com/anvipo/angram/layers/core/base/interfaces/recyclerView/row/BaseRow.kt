package com.anvipo.angram.layers.core.base.interfaces.recyclerView.row

import com.anvipo.angram.layers.core.IndexPath

interface BaseRow {

    val viewType: Int
    /**
     * Data about all views that can be presented by these rows
     */
    val viewsData: List<RowViewData>

    val indexPath: IndexPath

}