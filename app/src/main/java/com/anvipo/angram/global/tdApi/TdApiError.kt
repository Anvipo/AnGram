package com.anvipo.angram.global.tdApi

sealed class TdApiError : Error() {

    enum class Codes(val value: Int) {
        EMPTY_PHONE_NUMBER(8),
        PHONE_NUMBER_INVALID(400)
    }

    sealed class Custom(
        open val code: Int,
        override val message: String
    ) : TdApiError() {

        class EmptyPhoneNumber(
            override val code: Int,
            override val message: String
        ) : Custom(code, message)

        class PhoneNumberInvalid(
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