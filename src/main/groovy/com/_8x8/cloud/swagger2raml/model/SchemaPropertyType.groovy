package com._8x8.cloud.swagger2raml.model

/**
 * @author Jacek Kunicki
 */
abstract class SchemaPropertyType {

    String name
    boolean required = true

    static SchemaPropertyType primitive(String name) {
        return new PrimitiveSchemaProperty(name: name)
    }

    static SchemaPropertyType optionalPrimitive(String name) {
        return new PrimitiveSchemaProperty(name: name, required: false)
    }

    static SchemaPropertyType arrayOf(SchemaPropertyType type) {
        return new ArraySchemaProperty(itemType: type)
    }

    static SchemaPropertyType enumOf(Collection<String> allowedValues) {
        return new EnumSchemaProperty(allowedValues: allowedValues)
    }

    abstract extractSchema()
    
    protected static typedSchema(String type) {
        return [type: type]
    }
}

class PrimitiveSchemaProperty extends SchemaPropertyType {

    @Override
    def extractSchema() {
        return typedSchema(name) + [
                required: required
        ]
    }
}

class ObjectSchemaProperty extends SchemaPropertyType {

    Map<String, SchemaPropertyType> properties

    @Override
    def extractSchema() {
        return typedSchema('object') + [
                properties: properties.collectEntries { [(it.key): it.value.extractSchema()] }
        ]
    }
}

class ArraySchemaProperty extends SchemaPropertyType {

    SchemaPropertyType itemType

    @Override
    def extractSchema() {
        def itemSchema = itemType.extractSchema()
        itemSchema.remove('required')
        return typedSchema('array') + [
                item: itemSchema
        ]
    }
}

class EnumSchemaProperty extends SchemaPropertyType {

    Collection<String> allowedValues

    @Override
    def extractSchema() {
        return [
                enum: allowedValues
        ]
    }
}
