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
        Api api = readFromJson(json)

        def swaggerResourceReader = new SwaggerResourceReader()
        def actualResources = api.resources.collect { Resource resource ->
            swaggerResourceReader.readFromUrl(url + resource.path).children
        }

        def flattenedResources = flattenAndMerge(actualResources)
        api.resources = sortResources(flattenedResources)
        return api;
    }

    @Override
    Api readFromFile(File source) {
        def json = jsonSlurper.parse(source)
        return readFromJson(json)
    }

    private static Api readFromJson(json) {
        def api = new Api(
                title: json.info.title,
                version: json.apiVersion,
                resources: json.apis.collect { new Resource(path: it.path) }
        )

        api.resources = sortResources(api.resources)

        return api
    }

    private static Collection<Resource> sortResources(Collection<Resource> resources) {
        def sortedResources = resources.sort { resource1, resource2 ->
            resource1.path.compareTo(resource2.path)
        }

        sortedResources.each { resource ->
            resource.children = sortResources(resource.children)
            resource.methods.sort { method1, method2 -> method1.getClass().simpleName.compareTo(method2.getClass().simpleName) }
        }

        return sortedResources
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
