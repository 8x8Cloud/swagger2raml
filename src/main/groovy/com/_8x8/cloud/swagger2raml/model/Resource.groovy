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

    Resource mergeWith(Resource another) {
        if (another.path == this.path) {
            this.children.addAll(another.children)
            this.methods.addAll(another.methods)
        }

        return this
    }

    boolean equals(o) {
        if (this.is(o)) {
            return true
        }

        if (getClass() == o.class) {
            Resource resource = (Resource) o

            return children == resource.children && methods == resource.methods &&
                    path == resource.path
        }

        return false
    }

    int hashCode() {
        int result = path.hashCode()
        result = 31 * result + children.hashCode()
        result = 31 * result + methods.hashCode()
        return result
    }
}
