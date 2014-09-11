package com._8x8.cloud.swagger2raml.model
/**
 * @author Jacek Kunicki
 */
abstract class ModelPropertyType {

    String name

    abstract SchemaPropertyType toSchemaProperty(Map<String, Model> models)
}

class DirectModelProperty extends ModelPropertyType {

    @Override
    SchemaPropertyType toSchemaProperty(Map<String, Model> models) {
        return SchemaPropertyType.primitive(name)
    }
}

class ReferenceModelProperty extends ModelPropertyType {

    @Override
    SchemaPropertyType toSchemaProperty(Map<String, Model> models) {
        if (OptionalSupport.isOptional(name)) {
            return SchemaPropertyType.optionalPrimitive(OptionalSupport.actualType(name))
        } else {
            return new ObjectSchemaProperty(
                    name: name,
                    properties: models.get(name).properties.collectEntries {
                        [(it.key): it.value.toSchemaProperty(models)]
                    } as Map<String, SchemaPropertyType>
            )
        }
    }
}

class ArrayModelProperty extends ModelPropertyType {

    ModelPropertyType itemType

    @Override
    SchemaPropertyType toSchemaProperty(Map<String, Model> models) {
        return SchemaPropertyType.arrayOf(itemType.toSchemaProperty(models))
    }
}

class EnumModelProperty extends ModelPropertyType {

    Collection<String> allowedValues

    @Override
    SchemaPropertyType toSchemaProperty(Map<String, Model> models) {
        return SchemaPropertyType.enumOf(allowedValues)
    }
}