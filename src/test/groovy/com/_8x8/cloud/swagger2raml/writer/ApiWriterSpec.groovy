package com._8x8.cloud.swagger2raml.writer

import com._8x8.cloud.swagger2raml.model.Api
import com._8x8.cloud.swagger2raml.model.Delete
import com._8x8.cloud.swagger2raml.model.Get
import com._8x8.cloud.swagger2raml.model.Post
import com._8x8.cloud.swagger2raml.model.Put
import com._8x8.cloud.swagger2raml.model.QueryParameter
import com._8x8.cloud.swagger2raml.model.Resource
import groovy.transform.TypeChecked
import spock.lang.Specification

/**
 * @author Jacek Kunicki
 */
@TypeChecked
class ApiWriterSpec extends Specification {

    private static File prepareOutputFile() {
        File outputFile = new File('src/test/resources/output.raml')

        if (outputFile.exists()) {
            outputFile.delete()
        }

        return outputFile
    }

    def 'should write API to RAML file'() {
        setup:
        File outputFile = prepareOutputFile()

        def resources = [
                new Resource(
                        path: 'foo',
                        children: [
                                new Resource(
                                        path: 'foo1',
                                        methods: [
                                                new Post(),
                                                new Delete(description: 'delete foo1')
                                        ]
                                ),
                                new Resource(
                                        path: 'foo2',
                                        methods: [
                                                new Get(
                                                        description: 'get foo2',
                                                        queryParameters: [
                                                                new QueryParameter(
                                                                        name: 'filter',
                                                                        displayName: 'foo2 filter',
                                                                        type: 'string',
                                                                        description: 'value to filter by',
                                                                        example: 'xxx',
                                                                        required: false
                                                                )
                                                        ]
                                                )
                                        ]
                                )
                        ],
                        methods: [new Get(), new Post()]
                ),
                new Resource(
                        path: 'bar',
                        methods: [new Post(description: 'create bar')]
                ),
                new Resource(
                        path: 'baz',
                        methods: [new Put()]
                )
        ]
        def api = new Api(
                title: 'Test API',
                baseUri: 'http://example.com',
                version: '42',
                resources: resources
        )

        when:
        new ApiWriter(file: outputFile).writeApi(api)

        then:
        def expectedFileContents = new File('src/test/resources/expected.raml').text
        outputFile.getText() == expectedFileContents
    }
}