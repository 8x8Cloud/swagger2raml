package com._8x8.cloud.swagger2raml.model

/**
 * @author Jacek Kunicki
 */
abstract class SchemaPropertyType {

    String name = 'object'
    boolean required = true

    static SchemaPropertyType primitive(String name) {
        return new PrimitiveSchemaProperty(name: name)
    }

    static SchemaPropertyType optionalPrimitive(String name) {
        return new PrimitiveSchemaProperty(name: name, required: false)
    }
}

class PrimitiveSchemaProperty extends SchemaPropertyType {
}

class ObjectSchemaProperty extends SchemaPropertyType {
    Map<String, SchemaPropertyType> properties
}
