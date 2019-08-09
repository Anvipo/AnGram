package com.anvipo.angram.presentationLayer.common.interfaces.view.section

import com.anvipo.angram.presentationLayer.common.interfaces.view.BaseHeaderFooterData
import com.anvipo.angram.presentationLayer.common.interfaces.view.row.BaseRow

@Suppress("EXPERIMENTAL_API_USAGE", "unused")
interface BaseSection {

    val viewType: Int
    val viewsData: List<SectionViewData>

    val index: UInt

    val items: List<BaseRow>
    val headerData: BaseHeaderFooterData?

    val footerData: BaseHeaderFooterData?

}