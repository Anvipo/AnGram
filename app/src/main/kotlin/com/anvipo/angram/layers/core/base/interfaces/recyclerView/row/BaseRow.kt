package com.anvipo.angram.layers.core.base.interfaces.recyclerView.row

import com.anvipo.angram.layers.core.IndexPath

interface BaseRow {

    val viewType: Int
    val viewsData: List<RowViewData>

    val indexPath: IndexPath

}