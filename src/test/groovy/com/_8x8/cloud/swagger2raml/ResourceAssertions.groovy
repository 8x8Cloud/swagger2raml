package com._8x8.cloud.swagger2raml

import com._8x8.cloud.swagger2raml.model.Method
import com._8x8.cloud.swagger2raml.model.Resource

/**
 * @author Jacek Kunicki
 */
@Category(Resource)
class ResourceAssertions {

    Resource hasPath(String path) {
        assert this.path == path
        return this
    }

    Resource hasChildren(int count) {
        assert this.children.size() == count
        return this
    }

    Resource hasNoChildren() {
        assert this.children.empty
        return this
    }

    Resource hasMethods(Class<? extends Method>... methodClasses) {
        assert this.methods.size() == methodClasses.size()
        assert this.methods.every { it.class in methodClasses }
        return this
    }
}
