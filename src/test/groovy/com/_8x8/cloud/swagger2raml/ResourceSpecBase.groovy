package com._8x8.cloud.swagger2raml

import com._8x8.cloud.swagger2raml.model.Resource
import spock.lang.Specification

/**
 * @author Jacek Kunicki
 */
abstract class ResourceSpecBase extends Specification {

    def setupSpec() {
        Resource.metaClass.mixin ResourceAssertions
    }
}
