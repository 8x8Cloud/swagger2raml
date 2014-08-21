package com._8x8.cloud.swagger2raml

import com._8x8.cloud.swagger2raml.model.Api
import com._8x8.cloud.swagger2raml.model.Resource
import com._8x8.cloud.swagger2raml.reader.SwaggerApiReader
import com._8x8.cloud.swagger2raml.reader.SwaggerResourceReader
import com._8x8.cloud.swagger2raml.writer.ApiWriter
import spock.lang.Specification


class RamlGeneratorSpec extends Specification {

    private static final String ACTUAL_OUTPUT_FILE_NAME = 'src/test/resources/actual-output-2.raml'
    private static final String EXPECTED_OUTPUT_FILE_NAME = 'src/test/resources/expected-output-2.raml'
    private static final String INPUT_SWAGGER_RESOURCE_FILE = 'src/test/resources/swagger-resource.json'
    private static final String INPUT_SWAGGER_FILE = 'src/test/resources/swagger-api.json'

    def "should transform properly swagger json to raml"() {
        setup:
        def resourceFile = new File(INPUT_SWAGGER_RESOURCE_FILE)
        def swaggerApiFile1 = new File(INPUT_SWAGGER_FILE)
        Api api = new SwaggerApiReader().readFromFile(swaggerApiFile1)
        Resource resource = new SwaggerResourceReader().readFromFile(resourceFile)
        api.resources = [resource]

        def outputFile = RamlGenerator.prepareOutputFile(ACTUAL_OUTPUT_FILE_NAME)

        when:
        new ApiWriter(file: outputFile).writeApi(api)

        then:
        def expectedFileContents = new File(EXPECTED_OUTPUT_FILE_NAME).text
        outputFile.text == expectedFileContents
    }
}
