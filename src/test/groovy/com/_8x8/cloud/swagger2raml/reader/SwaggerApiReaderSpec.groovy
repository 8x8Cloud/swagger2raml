package com._8x8.cloud.swagger2raml.reader

import com._8x8.cloud.swagger2raml.model.Api
import spock.lang.Specification

/**
 * @author Jacek Kunicki
 */
class SwaggerApiReaderSpec extends Specification {

    def 'should read API from file'() {
        setup:
        File swaggerApiFile = new File('src/test/resources/swagger-api.json')

        when:
        Api api = new SwaggerApiReader().readFromFile(swaggerApiFile)

        then:
        !api.resources?.empty
    }
}
