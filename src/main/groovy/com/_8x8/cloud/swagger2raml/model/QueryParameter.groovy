package com._8x8.cloud.swagger2raml.model

import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * @author Jacek Kunicki
 */
@Canonical
@ToString(includePackage = false)
class QueryParameter {

    String name, displayName, type, description, example
    boolean required
    Boolean repeat
}
