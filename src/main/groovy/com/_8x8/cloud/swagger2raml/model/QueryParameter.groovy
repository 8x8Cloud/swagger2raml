package com._8x8.cloud.swagger2raml.model

import groovy.transform.ToString

/**
 * @author Jacek Kunicki
 */
@ToString(includePackage = false)
class QueryParameter {

    String name, displayName, type, description, example
    boolean required
    Boolean repeat
}
