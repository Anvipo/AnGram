package com.anvipo.angram.dataLayer.gateways.local.db.room.base

abstract class BaseConverter {

    protected fun transformClassAndHisFieldsWithTheirValuesToString(
        clazz: Class<*>,
        instanse: Any
    ): String {
        val fieldsAndTheirValues = clazz.declaredFields.map {
            it.isAccessible = true
            val result = it.name + fieldAndValueDelimiter + it.get(instanse)
            it.isAccessible = false
            return@map result
        }

        val fieldsAndTheirValuesString = fieldsAndTheirValues.joinToString(
            separator = fieldAndValuePairDelimiter,
            prefix = "",
            postfix = "",
            limit = -1
        )

        return "${clazz.simpleName}$classAndFieldsDelimiter$fieldsAndTheirValuesString"
    }

    protected fun getClassNameAndFieldsAndTheirValuesPairFrom(
        string: String
    ): Pair<String, List<String>> {
        val splittedString = string.split(classAndFieldsDelimiter)

        val className = splittedString[0]
        val fieldsAndTheirValues = splittedString[1]

        return className to fieldsAndTheirValues.split(fieldAndValuePairDelimiter)
    }

    protected fun split(fieldWithValueString: String): Pair<String, String> {
        val splittedString = fieldWithValueString.split(fieldAndValueDelimiter)
        val fieldName = splittedString[0]
        val fieldValue = splittedString[1]

        return fieldName to fieldValue
    }

    private val classAndFieldsDelimiter = ":"
    private val fieldAndValueDelimiter = "="
    private val fieldAndValuePairDelimiter = ","

}