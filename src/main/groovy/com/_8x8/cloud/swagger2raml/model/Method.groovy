package com._8x8.cloud.swagger2raml.model

import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * @author Jacek Kunicki
 */
@Canonical
@ToString(includePackage = false)
abstract class Method {

    String description
    Collection<QueryParameter> queryParameters = []
    Body body
    Collection<Body> responses = []

    static Method forType(String type) {
        String className = Method.class.package.name + '.' + type.toLowerCase().capitalize()
        return Method.classLoader.loadClass(className).newInstance() as Method
    }

    def copy() {
        def method = forType(this.class.simpleName)
        method.description = this.description
        method.responses.addAll(responses)
        method.queryParameters.addAll(queryParameters)
        return method
    }
}

class Get extends Method {
}

class Post extends Method {
}

class Put extends Method {
}

class Delete extends Method {
}

class Patch extends Method {
}

class Head extends Method {
}

class Options extends Method {
}

class Trace extends Method {
}