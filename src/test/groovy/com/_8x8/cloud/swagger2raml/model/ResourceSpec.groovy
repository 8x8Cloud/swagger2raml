package com._8x8.cloud.swagger2raml.model

import spock.lang.Specification

/**
 * @author Jacek Kunicki
 */
class ResourceSpec extends Specification {

    Resource resource = new Resource(
            path: '',
            children: [
                    new Resource(
                            path: 'a',
                            children: [
                                    new Resource(path: 'b')
                            ]
                    ),
                    new Resource(path: 'c')
            ]
    )

    def 'should find child by valid path'() {
        when:
        Resource childByPath = resource.childByPath(path)

        then:
        childByPath.path == expectedPath

        where:
        path   || expectedPath
        '/a'   || 'a'
        '/a/b' || 'b'
        '/c'   || 'c'
    }

    def 'should not find child by invalid path'() {
        expect:
        resource.childByPath(path) == null

        where:
        path << ['/foo', '/a/d', '/a/b/c']
    }
}
