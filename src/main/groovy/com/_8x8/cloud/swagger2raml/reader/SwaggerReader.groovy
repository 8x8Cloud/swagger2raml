package com._8x8.cloud.swagger2raml.reader

import groovy.json.JsonSlurper

/**
 * @author Jacek Kunicki
 */
abstract class SwaggerReader<T> {

    protected JsonSlurper jsonSlurper = new JsonSlurper()

    abstract T readFromFile(File file)

    abstract T readFromUrl(String url)
}
