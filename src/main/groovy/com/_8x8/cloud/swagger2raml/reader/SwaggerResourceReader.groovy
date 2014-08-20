package com._8x8.cloud.swagger2raml.reader

import com._8x8.cloud.swagger2raml.model.ArrayModelProperty
import com._8x8.cloud.swagger2raml.model.Body
import com._8x8.cloud.swagger2raml.model.BodySchema
import com._8x8.cloud.swagger2raml.model.DirectModelProperty
import com._8x8.cloud.swagger2raml.model.EnumModelProperty
import com._8x8.cloud.swagger2raml.model.Method
import com._8x8.cloud.swagger2raml.model.Model
import com._8x8.cloud.swagger2raml.model.ModelPropertyType
import com._8x8.cloud.swagger2raml.model.ObjectSchemaProperty
import com._8x8.cloud.swagger2raml.model.Path
import com._8x8.cloud.swagger2raml.model.QueryParameter
import com._8x8.cloud.swagger2raml.model.ReferenceModelProperty
import com._8x8.cloud.swagger2raml.model.Resource
import com._8x8.cloud.swagger2raml.model.SchemaProperty
import com._8x8.cloud.swagger2raml.model.SchemaPropertyType

import java.util.regex.Pattern

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
        Map<String, Model> models = extractModels(json)
        json.apis.each { swaggerResource ->
            Resource newResource = new Resource(methods: extractMethods(swaggerResource, models))
            addResource(root, new Path(swaggerResource.path), newResource)
        }
        return root
    }

    private static Map<String, Model> extractModels(json) {
        return json.models?.values()?.collect { modelValues ->
            return new Model(
                    id: modelValues.id,
                    properties: modelValues.properties.collectEntries { property ->
                        return [(property.key): extractModelPropertyType(property.value)]
                    } as Map<String, ModelPropertyType>
            )
        }?.collectEntries { [(it.id): it] }
    }

    private static ModelPropertyType extractModelPropertyType(propertyValue) {
        if (propertyValue.$ref) {
            return new ReferenceModelProperty(name: propertyValue.$ref)
        } else if (propertyValue.enum) {
            return new EnumModelProperty(allowedValues: propertyValue.enum)
        } else if (propertyValue.type == 'array') {
            return new ArrayModelProperty(
                    name: propertyValue.type,
                    itemType: extractModelPropertyType(propertyValue.items)
            )
        } else {
            return new DirectModelProperty(name: propertyValue.type)
        }
    }

    private static Collection<Method> extractMethods(swaggerResource, Map<String, Model> models) {
        return swaggerResource.operations.collect { operation ->
            Method method = Method.forType(operation.method)
            method.description = operation.summary
            method.queryParameters = extractQueryParameters(operation)
            method.responses = extractResponses(operation)

            def bodyParameter = operation.parameters.find { it.paramType == 'body' }
            if (bodyParameter) {
                method.body = extractBody(bodyParameter, models)
            }

            return method
        }
    }

    private static Body extractBody(bodyParameter, Map<String, Model> models) {
        Collection<SchemaProperty> schemaProperties = []
        if (models && models[bodyParameter.type]) {
            schemaProperties = extractSchemaProperties(models[bodyParameter.type].properties, models)
        }

        return new Body(schema: new BodySchema(properties: schemaProperties))
    }

    private static Collection<SchemaProperty> extractSchemaProperties(Map<String, ModelPropertyType> modelPropertiesByName, Map<String, Model> models) {
        return modelPropertiesByName.collect {
            return new SchemaProperty(name: it.key, type: it.value.toSchemaProperty(models))
        }
    }

    private static Collection<QueryParameter> extractQueryParameters(operation) {
        return operation.parameters.findAll { it.paramType == 'query' }.collect { parameter ->
            QueryParameter queryParameter = new QueryParameter(
                    name: parameter.name,
                    displayName: parameter.name,
                    description: parameter.description,
                    type: parameter.type,
                    required: parameter.required
            )

            if (parameter.type == 'array') {
                queryParameter.with {
                    type = 'string'
                    repeat = true
                }
            }

            return queryParameter
        }
    }

    private static Collection<Body> extractResponses(operation) {
        return operation.produces.collect { new Body(contentType: it) }
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

        node.methods = node.methods.groupBy { method -> method.getClass().simpleName }.collect {
            map ->
                if (map.getValue().size() > 1) {
                    return map.getValue().inject { method1, method2 ->
                        def method = method1.copy()

                        method.description += " " + method2.description
                        method.responses.addAll(method2.responses)
                        return method
                    }
                }
                return map.getValue().get(0)
        }
    }
}

