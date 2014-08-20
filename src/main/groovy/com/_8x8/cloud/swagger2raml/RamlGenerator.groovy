package com._8x8.cloud.swagger2raml

import com._8x8.cloud.swagger2raml.model.Api
import com._8x8.cloud.swagger2raml.reader.SwaggerApiReader
import com._8x8.cloud.swagger2raml.writer.ApiWriter

/**
 * @author Jacek Kunicki
 */
class RamlGenerator {

    static void main(String[] args) {
        CliBuilder cli = new CliBuilder(usage: 'swagger2raml [options] <swaggerApiUrl>')
        cli.o(longOpt: 'output', args: 1, argName: 'file', 'output file name, default: api.raml')
        OptionAccessor options = cli.parse(args)

        if (options?.arguments()?.empty) {
            cli.usage()
            return
        }

        String swaggerUrl = options.arguments().first()
        String outputFileName = options.o ?: 'api.raml'
        generateFromSwaggerUrl(swaggerUrl, outputFileName)

        println "Generated RAML file: ${outputFileName}"
    }

    static File generateFromSwaggerUrl(String url, String outputFileName) {
        Api api = new SwaggerApiReader().readFromUrl(url)

        def outputFile = prepareOutputFile(outputFileName)

        new ApiWriter(file: outputFile).writeApi(api)

        return outputFile
    }

    static File prepareOutputFile(String outputFileName) {
        File outputFile = new File(outputFileName)
        if (outputFile.exists()) {
            outputFile.delete()
        }
        return outputFile
    }
}
