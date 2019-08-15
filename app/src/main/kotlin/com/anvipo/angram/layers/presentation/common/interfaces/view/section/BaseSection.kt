package com.anvipo.angram.layers.presentation.common.interfaces.view.section

import com.anvipo.angram.layers.presentation.common.interfaces.view.BaseHeaderFooterData
import com.anvipo.angram.layers.presentation.common.interfaces.view.row.BaseRow

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