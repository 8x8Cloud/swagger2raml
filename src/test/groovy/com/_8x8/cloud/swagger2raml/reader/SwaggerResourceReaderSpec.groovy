package com._8x8.cloud.swagger2raml.reader

import com._8x8.cloud.swagger2raml.ResourceSpecBase
import com._8x8.cloud.swagger2raml.model.Delete
import com._8x8.cloud.swagger2raml.model.Get
import com._8x8.cloud.swagger2raml.model.Path
import com._8x8.cloud.swagger2raml.model.Post
import com._8x8.cloud.swagger2raml.model.Put
import com._8x8.cloud.swagger2raml.model.Resource

/**
 * @author Jacek Kunicki
 */
class SwaggerResourceReaderSpec extends ResourceSpecBase {

    def 'should read single resource'() {
        setup:
        File resourceFile = new File('src/test/resources/swagger-resource.json')

        when:
        Resource rootResource = new SwaggerResourceReader().readFromFile(resourceFile)

        then:
        with(rootResource) {
            children.size() == 2
            childByPath('/ad').hasPath('ad').hasChildren(3)
            childByPath('/ad/conditionalDnsForwarder').hasPath('conditionalDnsForwarder').hasChildren(1).hasMethods(Get, Post)
            childByPath('/ad/conditionalDnsForwarder/{remoteDomainName}').hasPath('{remoteDomainName}').hasNoChildren()
            childByPath('/ad/domain').hasPath('domain').hasChildren(2)
            childByPath('/ad/domain/{domain}').hasPath('{domain}').hasChildren(1)
            childByPath('/ad/domain/{domain}/pc').hasPath('pc').hasChildren(0).hasMethods(Post)
            childByPath('/ad/domain/passwordPolicy').hasPath('passwordPolicy').hasNoChildren().hasMethods(Get)
            childByPath('/ad/join').hasPath('join').hasNoChildren().hasMethods(Post)
            childByPath('/vdi').hasPath('vdi').hasChildren(2)
            childByPath('/vdi/ad').hasPath('ad').hasChildren(1).hasMethods(Post, Put, Delete)
            childByPath('/vdi/ad/dhcpscope').hasPath('dhcpscope').hasNoChildren().hasMethods(Post, Put, Delete)
            childByPath('/vdi/ad_cert_service').hasPath('ad_cert_service').hasNoChildren().hasMethods(Post)
        }
    }

    def 'should build resource prefix tree from paths'() {
        setup:
        Resource rootResource = new Resource()
        Collection<Path> paths = ['/a/b/c', '/a/d', '/a/b/e', '/f/g', '/f/h'].collect { new Path(it) }

        when:
        paths.each { SwaggerResourceReader.addResource(rootResource, it, new Resource()) }

        then:
        with(rootResource) {
            children.size() == 2
            childByPath('/a').hasPath('a').hasChildren(2)
            childByPath('/a/b').hasPath('b').hasChildren(2)
            childByPath('/a/b/c').hasPath('c').hasNoChildren()
            childByPath('/a/b/e').hasPath('e').hasNoChildren()
            childByPath('/a/d').hasPath('d').hasNoChildren()
            childByPath('/f').hasPath('f').hasChildren(2)
            childByPath('/f/g').hasPath('g').hasNoChildren()
            childByPath('/f/h').hasPath('h').hasNoChildren()
        }
    }
}

