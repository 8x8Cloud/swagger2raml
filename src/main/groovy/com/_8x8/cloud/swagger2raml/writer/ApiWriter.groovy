package com._8x8.cloud.swagger2raml.writer

import groovy.transform.TypeChecked
import com._8x8.cloud.swagger2raml.model.Api

/**
 * @author Jacek Kunicki
 */
@TypeChecked
class ApiWriter {

    private File file

    void writeApi(Api api) {
        new RamlWriter(file: file).with {
            writeHeader()
            writeProperties(api, ['title', 'baseUri', 'version'])
            writeResources(api.resources)
            save()
        }
    }
}