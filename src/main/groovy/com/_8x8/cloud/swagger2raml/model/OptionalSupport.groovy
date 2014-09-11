package com._8x8.cloud.swagger2raml.model

import java.util.regex.Pattern

/**
 * @author Jacek Kunicki
 */
class OptionalSupport {

    private static final Pattern OPTIONAL_PATTERN = ~/Optional«(\w+)»/

    static String actualTypeIfOptional(String originalType) {
        return isOptional(originalType) ? actualType(originalType) : originalType
    }

    static boolean isOptional(String name) {
        return name.matches(OPTIONAL_PATTERN)
    }

    static String actualType(String originalType) {
        return originalType.replaceAll(OPTIONAL_PATTERN, '$1')
    }
}
