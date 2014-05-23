package com._8x8.cloud.swagger2raml.reader

import com._8x8.cloud.swagger2raml.model.Method
import com._8x8.cloud.swagger2raml.model.Path
import com._8x8.cloud.swagger2raml.model.QueryParameter
import com._8x8.cloud.swagger2raml.model.Resource

/**
 * @author Jacek Kunicki
 */
class SwaggerResourceReader extends SwaggerReader<Resource> {

    @Override
    Resource readFromUrl(String url) {
        def json = jsonSlurper.parse(new URL(url))
        return readFromJson(json)
    }

    @Override
    Resource readFromFile(File source) {
        def json = jsonSlurper.parse(source)
        return readFromJson(json)
    }

    private static Resource readFromJson(json) {
        def rootPath = json.apis.first().path.split('/').first()
        Resource root = new Resource(path: rootPath)
        json.apis.each { swaggerResource ->
            Resource newResource = new Resource(methods: extractMethods(swaggerResource))
            addResource(root, new Path(swaggerResource.path), newResource)
        }
        return root
    }

    private static Collection<Method> extractMethods(swaggerResource) {
        return swaggerResource.operations.collect { operation ->
            Method method = Method.forType(operation.method)
            method.description = operation.summary
            method.queryParameters = extractQueryParameters(operation)
            return method
        }
    }

    private static Collection<QueryParameter> extractQueryParameters(Object operation) {
        return operation.parameters.findAll { it.paramType == 'query' }.collect { parameter ->
            return new QueryParameter(
                    name: parameter.name,
                    displayName: parameter.name,
                    description: parameter.description,
                    type: parameter.type,
                    required: parameter.required
            )
        }
    }

    private static void addResource(Resource root, Path path, Resource newResource) {
        Resource node = root

        int lastPresentIndex = 0
        boolean pathAlreadyExists = true
        for (i in lastPresentIndex..<path.length()) {
            def childByPath = node.childByPath(path.elementAt(i))
            if (childByPath) {
                node = childByPath
            } else {
                lastPresentIndex = i
                pathAlreadyExists = false
                break
            }
        }

        if (!pathAlreadyExists) {
            for (i in lastPresentIndex..<path.length()) {
                Resource newNode = new Resource(path: path.elementAt(i))
                node.children.add(newNode)
                node = newNode
            }
        }

        node.methods.addAll(newResource.methods)
    }
}

