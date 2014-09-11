package com._8x8.cloud.swagger2raml.model

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Jacek Kunicki
 */
class QueryParameterSpec extends Specification {

    private static final PARAMETER_BASE = [
            name       : 'foo',
            displayName: 'bar',
            description: 'baz',
            required   : false
    ]

    @Unroll
    def 'should correctly determine parameter type for #originalType'() {
        expect:
        QueryParameter.create(PARAMETER_BASE + [type: originalType]).type == expectedType

        where:
        originalType       || expectedType
        'string'           || 'string'
        'integer'          || 'integer'
        'Optional«string»' || 'string'
        'long'             || 'integer'
        'Optional«long»'   || 'integer'
        'foo'              || 'string'
        'Optional«bar»'    || 'string'
    }
}
