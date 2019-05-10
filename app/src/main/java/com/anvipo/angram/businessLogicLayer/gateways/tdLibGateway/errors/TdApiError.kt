@file:Suppress("unused")

package com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors

sealed class TdApiError : Error() {

    enum class Codes(val value: Int) {
        EMPTY_PARAMETER(8),
        BAD_REQUEST(400),
        DATABASE_ENCRYPTION_KEY_IS_NEEDED(401)
    }

    sealed class Custom(
        open val code: Int,
        override val message: String
    ) : TdApiError() {

        class EmptyParameter(
            override val code: Int,
            override val message: String
        ) : Custom(code, message)

        class BadRequest(
            override val code: Int,
            override val message: String
        ) : Custom(code, message)

        class DatabaseEncryptionKeyIsNeeded(
            override val code: Int,
            override val message: String
        ) : Custom(code, message)


        class Unspecified(
            override val code: Int,
            override val message: String
        ) : Custom(code, message)

    }

    object Unspecified : TdApiError()

}