package com._8x8.cloud.swagger2raml.reader

import com._8x8.cloud.swagger2raml.ResourceSpecBase
import com._8x8.cloud.swagger2raml.model.Api
import com._8x8.cloud.swagger2raml.model.Delete
import com._8x8.cloud.swagger2raml.model.Get
import com._8x8.cloud.swagger2raml.model.Post
import com._8x8.cloud.swagger2raml.model.Put
import com._8x8.cloud.swagger2raml.model.Resource

/**
 * @author Jacek Kunicki
 */
class SwaggerApiReaderSpec extends ResourceSpecBase {

    def 'should read API from file'() {
        setup:
        File swaggerApiFile = new File('src/test/resources/swagger-api.json')

        when:
        Api api = new SwaggerApiReader().readFromFile(swaggerApiFile)

        then:
        !api.resources?.empty
    }

    def 'should read the same API when swagger order is different'() {
        setup:
        File swaggerApiFile1 = new File('src/test/resources/swagger-api.json')
        File swaggerApiFile2 = new File('src/test/resources/swagger-api-2.json')

        when:
        Api api1 = new SwaggerApiReader().readFromFile(swaggerApiFile1)
        Api api2 = new SwaggerApiReader().readFromFile(swaggerApiFile2)

        then:
        api1.resources == api2.resources
    }

    def 'should merge resources by path recursively'() {
        setup:
        Collection<Resource> resources = [
                new Resource(path: 'a', methods: [new Get()], children: [
                        new Resource(path: 'b', methods: [new Get()]),
                        new Resource(path: 'b', methods: [new Post()])
                ]),
                new Resource(path: 'a', methods: [new Delete()], children: [
                        new Resource(path: 'b', methods: [new Put()])
                ]),
                new Resource(path: 'c', methods: [new Get()])
        ]

        when:
        Collection<Resource> mergedResources = SwaggerApiReader.merge(resources)

        then:
        Resource resourceA = mergedResources.find { it.path == 'a' }
        resourceA.hasChildren(1).hasMethods(Get, Delete)

        and:
        resourceA.childByPath('b').hasNoChildren().hasMethods(Get, Post, Put)

        and:
        mergedResources.find { it.path == 'c' }.hasNoChildren().hasMethods(Get)
    }
}
