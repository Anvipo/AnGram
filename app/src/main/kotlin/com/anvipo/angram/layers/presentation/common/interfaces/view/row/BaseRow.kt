package com.anvipo.angram.layers.presentation.common.interfaces.view.row

import com.anvipo.angram.layers.core.IndexPath

@Suppress("EXPERIMENTAL_API_USAGE")
interface BaseRow {

    val viewType: Int
    val viewsData: List<RowViewData>

    val indexPath: IndexPath

}