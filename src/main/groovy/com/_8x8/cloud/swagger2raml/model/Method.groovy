package com._8x8.cloud.swagger2raml.model

/**
 * @author Jacek Kunicki
 */
abstract class Method {

    String description
    Collection<QueryParameter> queryParameters
}

class Get extends Method {
}

class Post extends Method {
}

class Put extends Method {
}

class Delete extends Method {
}