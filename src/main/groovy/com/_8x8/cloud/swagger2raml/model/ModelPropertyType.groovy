package com._8x8.cloud.swagger2raml.model

/**
 * @author Jacek Kunicki
 */
abstract class ModelPropertyType {

    String name
}

class DirectModelProperty extends ModelPropertyType {
}

class ReferenceModelProperty extends ModelPropertyType {
}

class ArrayModelProperty extends ModelPropertyType {
    ModelPropertyType itemType
}