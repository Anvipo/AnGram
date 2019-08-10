@file:Suppress("unused")

package com.anvipo.angram.layers.data.gateways.tdLib.errors

open class TdApiError(
    override val message: String?,
    val code: Int
) : Error() {

    object Unspecified : TdApiError(message = null, code = -1)

}
