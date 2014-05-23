package com._8x8.cloud.swagger2raml.model

import groovy.transform.ToString

/**
 * @author Jacek Kunicki
 */
@ToString(includePackage = false)
class Resource {

    String path
    Collection<Resource> children = []
    Collection<? extends Method> methods = []

    Resource childByPath(String path) {
        return childByPath(new Path(path))
    }

    Resource childByPath(Path path) {
        Resource child = children.find { it.path == path.first() }
        if (path.rest()) {
            return child?.childByPath(path.rest())
        } else {
            return child
        }
    }
}
