package com._8x8.cloud.swagger2raml.reader

import com._8x8.cloud.swagger2raml.model.Api
import com._8x8.cloud.swagger2raml.model.Resource

/**
 * @author Jacek Kunicki
 */
class SwaggerApiReader extends SwaggerReader<Api> {

    @Override
    Api readFromUrl(String url) {
        def json = jsonSlurper.parse(new URL(url))
        return readFromJson(json)
    }

    @Override
    Api readFromFile(File source) {
        def json = jsonSlurper.parse(source)
        return readFromJson(json)
    }

    private static Api readFromJson(json) {
        return new Api(
                title: json.info.title,
                version: json.apiVersion,
                resources: json.apis.collect { new Resource(path: it.path) }
        )
    }
}
