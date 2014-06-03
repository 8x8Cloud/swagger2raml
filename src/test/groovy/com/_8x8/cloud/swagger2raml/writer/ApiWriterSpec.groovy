package com._8x8.cloud.swagger2raml.writer

import com._8x8.cloud.swagger2raml.model.Api
import com._8x8.cloud.swagger2raml.model.Body
import com._8x8.cloud.swagger2raml.model.BodySchema
import com._8x8.cloud.swagger2raml.model.Delete
import com._8x8.cloud.swagger2raml.model.Get
import com._8x8.cloud.swagger2raml.model.Post
import com._8x8.cloud.swagger2raml.model.Put
import com._8x8.cloud.swagger2raml.model.QueryParameter
import com._8x8.cloud.swagger2raml.model.Resource
import com._8x8.cloud.swagger2raml.model.SchemaProperty
import groovy.json.JsonBuilder
import org.raml.parser.visitor.RamlValidationService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * @author Jacek Kunicki
 */
@Stepwise
class ApiWriterSpec extends Specification {

    private static final String ACTUAL_OUTPUT_FILE_NAME = 'src/test/resources/actual-output.raml'
    private static final String EXPECTED_OUTPUT_FILE_NAME = 'src/test/resources/expected-output.raml'

    @Shared
    private File outputFile

    def setupSpec() {
        outputFile = new File(ACTUAL_OUTPUT_FILE_NAME)

        if (outputFile.exists()) {
            outputFile.delete()
        }
    }

    def 'should write API to RAML file'() {
        setup:
        BodySchema bodySchema = new BodySchema(
                properties: [
                        new SchemaProperty(name: 'foo', type: 'string'),
                        new SchemaProperty(name: 'bar', type: 'string', required: false)
                ]
        )

        def bodyExample = [
                foo: 'xxx',
                bar: 'yyy'
        ]

        def resources = [
                new Resource(
                        path: 'foo',
                        children: [
                                new Resource(
                                        path: 'foo1',
                                        methods: [
                                                new Post(
                                                        body: new Body(
                                                                schema: bodySchema,
                                                                example: new JsonBuilder(bodyExample).toPrettyString()
                                                        )
                                                ),
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
        def expectedFileContents = new File(EXPECTED_OUTPUT_FILE_NAME).text
        outputFile.text == expectedFileContents
    }

    def 'generated RAML file should be valid'() {
        expect:
        RamlValidationService.createDefault().validate("file:${ACTUAL_OUTPUT_FILE_NAME}").empty
    }
}