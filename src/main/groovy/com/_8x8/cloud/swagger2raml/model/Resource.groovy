package com._8x8.cloud.swagger2raml.model

/**
 * @author Jacek Kunicki
 */
class Resource {

    String path
    Collection<Resource> children
    Collection<? extends Method> methods
}
