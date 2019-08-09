package com.anvipo.angram.presentationLayer.common.interfaces.view.row

import com.anvipo.angram.coreLayer.IndexPath

@Suppress("EXPERIMENTAL_API_USAGE")
interface BaseRow {

    val viewType: Int
    val viewsData: List<RowViewData>

    val indexPath: IndexPath

}