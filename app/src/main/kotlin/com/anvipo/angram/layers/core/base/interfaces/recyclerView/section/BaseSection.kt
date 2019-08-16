package com.anvipo.angram.layers.core.base.interfaces.recyclerView.section

import com.anvipo.angram.layers.core.base.interfaces.recyclerView.BaseHeaderFooterData
import com.anvipo.angram.layers.core.base.interfaces.recyclerView.row.BaseRow

@Suppress("unused")
interface BaseSection {

    val viewType: Int
    val viewsData: List<SectionViewData>

    @ExperimentalUnsignedTypes
    val index: UInt

    val items: List<BaseRow>
    val headerData: BaseHeaderFooterData?

    val footerData: BaseHeaderFooterData?

}