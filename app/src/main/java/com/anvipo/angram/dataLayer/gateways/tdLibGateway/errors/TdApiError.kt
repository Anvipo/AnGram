@file:Suppress("unused")

package com.anvipo.angram.dataLayer.gateways.tdLibGateway.errors

open class TdApiError(
    override val message: String?,
    val code: Int
) : Error() {

    object Unspecified : TdApiError(message = null, code = -1)

}

/*
sealed class TdApiError : Error() {

    enum class Codes(val value: Int) {
        EMPTY_PARAMETER(8),
        BAD_REQUEST(400),
        DATABASE_ENCRYPTION_KEY_IS_NEEDED(401),
        TOO_MANY_REQUESTS(429)
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

        class TooManyRequests(
            override val code: Int,
            override val message: String
        ) : Custom(code, message)


        class Unspecified(
            override val code: Int,
            override val message: String
        ) : Custom(code, message)

    }

    object Unspecified : TdApiError()

}*/
