package com._8x8.cloud.swagger2raml.writer

import com._8x8.cloud.swagger2raml.model.Path
import groovy.transform.TypeChecked
import com._8x8.cloud.swagger2raml.model.Method
import com._8x8.cloud.swagger2raml.model.QueryParameter
import com._8x8.cloud.swagger2raml.model.Resource

/**
 * @author Jacek Kunicki
 */
@TypeChecked
class RamlWriter {

    private static final String LINE_SEPARATOR = System.getProperty('line.separator')
    private static final String RAML_HEADER = '#%RAML 0.8'
    private static final int TAB_WIDTH = 2

    private int indentation = 0

    private File file

    void writeHeader() {
        write(RAML_HEADER)
        write('---')
    }

    void writeResources(Collection<Resource> resources) {
        resources.each { resource ->
            write("${Path.SEPARATOR}${resource.path}:")
            indented {
                resource.methods.each { writeMethod(it) }
                writeResources(resource.children)
            }
        }
    }

    private void writeMethod(Method method) {
        write("${method.class.simpleName.toLowerCase()}:")

        indented {
            if (method.description) {
                writeProperty(method, 'description')
            }

            if (method.queryParameters) {
                write('queryParameters:')
                indented {
                    method.queryParameters.each { writeQueryParameter(it) }
                }
            }
        }
    }

    private void writeQueryParameter(QueryParameter queryParameter) {
        write("${queryParameter.name}:")
        indented {
            writeProperties(queryParameter, ['displayName', 'type', 'description', 'example', 'required', 'repeat'])
        }
    }

    void writeProperties(object, Collection<String> propertyNames) {
        propertyNames.each { writeProperty(object, it) }
    }

    private void writeProperty(object, String propertyName) {
        // explicit null check (instead of Groovy truth) since a false value is valid
        if (object[propertyName] != null) {
            write("${propertyName}: ${object[propertyName]}")
        }
    }

    private void write(String s) {
        file << ' ' * indentation << s << LINE_SEPARATOR
    }

    private void indent() {
        indentation += TAB_WIDTH
    }

    private void unident() {
        indentation -= TAB_WIDTH
    }

    private void indented(Closure closure) {
        indent()
        closure.call()
        unident()
    }
}
