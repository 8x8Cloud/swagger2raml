package com._8x8.cloud.swagger2raml

import com._8x8.cloud.swagger2raml.model.Delete
import com._8x8.cloud.swagger2raml.model.Get
import com._8x8.cloud.swagger2raml.model.Post
import com._8x8.cloud.swagger2raml.model.Put
import com._8x8.cloud.swagger2raml.model.Resource

/**
 * @author Jacek Kunicki
 */
class RamlGeneratorSpec extends ResourceSpecBase {

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
        Collection<Resource> mergedResources = RamlGenerator.merge(resources)

        then:
        Resource resourceA = mergedResources.find { it.path == 'a' }
        resourceA.hasChildren(1).hasMethods(Get, Delete)

        and:
        resourceA.childByPath('b').hasNoChildren().hasMethods(Get, Post, Put)

        and:
        mergedResources.find { it.path == 'c' }.hasNoChildren().hasMethods(Get)
    }
}
