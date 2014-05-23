package com._8x8.cloud.swagger2raml.model

import groovy.transform.ToString

/**
 * @author Jacek Kunicki
 */
@ToString(includePackage = false)
class Path {

    static final String SEPARATOR = '/'

    private List<String> tokens

    Path(String value) {
        List<String> split = value.split(SEPARATOR)
        int firstToken = value.startsWith('/') ? 1 : 0
        this.tokens = split[firstToken..<split.size()]
    }

    private Path(List<String> tokens) {
        this.tokens = tokens
    }

    int length() {
        return tokens.size()
    }

    String first() {
        return elementAt(0)
    }

    String elementAt(int index) {
        return tokens[index]
    }

    Path rest() {
        if (length() == 1) {
            return null
        } else {
            return new Path(tokens[1..<length()])
        }
    }

    @Override
    String toString() {
        return tokens.join(SEPARATOR)
    }
}
