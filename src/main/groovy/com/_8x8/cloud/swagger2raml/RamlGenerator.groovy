package com._8x8.cloud.swagger2raml

import com._8x8.cloud.swagger2raml.model.Api
import com._8x8.cloud.swagger2raml.model.Resource
import com._8x8.cloud.swagger2raml.reader.SwaggerApiReader
import com._8x8.cloud.swagger2raml.reader.SwaggerResourceReader
import com._8x8.cloud.swagger2raml.writer.ApiWriter

/**
 * @author Jacek Kunicki
 */
class RamlGenerator {

    static File generateFromSwaggerUrl(String url, String outputFileName) {
        Api api = new SwaggerApiReader().readFromUrl(url)

        SwaggerResourceReader swaggerResourceReader = new SwaggerResourceReader()
        def actualResources = api.resources.collect { Resource resource ->
            swaggerResourceReader.readFromUrl(url + resource.path).children
        }

        api.resources = flattenAndMerge(actualResources)

        File outputFile = new File(outputFileName)
        if (outputFile.exists()) {
            outputFile.delete()
        }

        new ApiWriter(file: outputFile).writeApi(api)

        return outputFile
    }

    private static Collection<Resource> flattenAndMerge(Collection<Collection<Resource>> resources) {
        Collection<Resource> flattened = resources.flatten()
        return merge(flattened)
    }

    private static Collection<Resource> merge(Collection<Resource> resources) {
        Map<String, List<Resource>> resourcesByPath = resources.groupBy { it.path }

        Collection<Resource> mergedResources = []
        resourcesByPath.keySet().each { key ->
            Resource mergedResource = new Resource(path: key)
            resourcesByPath[key].each { resource ->
                mergedResource.mergeWith(resource)
            }
            mergedResources.add(mergedResource)
        }

        mergedResources.each {
            it.children = merge(it.children)
        }

        return mergedResources
    }
}
