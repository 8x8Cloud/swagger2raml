package com._8x8.cloud.swagger2raml.model

import groovy.transform.Canonical
import groovy.transform.ToString
import groovy.util.logging.Log

/**
 * @author Jacek Kunicki
 */
@Canonical
@ToString(includePackage = false)
@Log
class QueryParameter {

    private static final String[] SUPPORTED_TYPES = ['string', 'number', 'integer', 'file', 'date', 'boolean', 'array']

    String name, displayName, type, description, example
    boolean required
    Boolean repeat

    static QueryParameter create(parameter) {
        String parameterType = OptionalSupport.actualTypeIfOptional(parameter.type)

        parameterType = longToInteger(parameterType)
        parameterType = unsupportedTypeToString(parameterType)

        QueryParameter queryParameter = new QueryParameter(
                name: parameter.name,
                displayName: parameter.name,
                description: parameter.description,
                type: parameterType,
                required: parameter.required
        )

        if (parameterType == 'array') {
            queryParameter.with {
                type = 'string'
                repeat = true
            }
        }

        return queryParameter
    }

    private static String unsupportedTypeToString(String parameterType) {
        if (!(parameterType in SUPPORTED_TYPES)) {
            log.warning("Parameter type is ${parameterType} " +
                    "but has to be one of: ${SUPPORTED_TYPES.join(', ')}. Setting string instead.")
            parameterType = 'string'
        }

        return parameterType
    }

    private static String longToInteger(String parameterType) {
        if (parameterType == 'long') {
            parameterType = 'integer'
        }

        return parameterType
    }
}
