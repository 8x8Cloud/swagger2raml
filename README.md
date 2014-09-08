# swagger2raml
A utility to generate [RAML](http://raml.org/) documentation from [Swagger](https://helloreverb.com/developers/swagger) JSON. 

## Usage
swagger2raml can be used either from command line (requires Java to be on the `PATH`) or as a Java library.

### Command line
```
./swagger2raml [-o outputFileName] <swaggerApiUrl>
```
where:
- `outputFileName` is an optional parameter to specify the RAML file name (default is `api.raml`),
- `swaggerApiUrl` is the URL of the Swagger API endpoint which provides resource listing as JSON.

### Your application
```java
RamlGenerator.generateFromSwaggerUrl(String url, String outputFileName)
```
method with the parameters as described in the command line usage.

Grab the JAR from Maven or use the bundled one:

#### Gradle
```groovy
repositories {
    maven { url 'https://nexus.softwaremill.com/content/repositories/snapshots' }
}

compile 'com._8x8.cloud.swagger2raml:swagger2raml:1.0-SNAPSHOT'
```

#### Maven
```xml
<distributionManagement>
    <snapshotRepository>
        <id>snapshots</id>
        <url>https://nexus.softwaremill.com/content/repositories/snapshots</url>
    </snapshotRepository>
</distributionManagement>

<dependencies>
    <dependency>
        <groupId>com._8x8.cloud.swagger2raml</groupId>
        <artifactId>swagger2raml</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

#### Bundled JAR
Add `dist/swagger2raml.jar` to your `CLASSPATH`

### Contributing to this project

Anyone and everyone is welcome to contribute. Please take a moment to
review the [guidelines for contributing](CONTRIBUTING.md).

* [Bug reports](CONTRIBUTING.md#bugs)
* [Feature requests](CONTRIBUTING.md#features)
* [Pull requests](CONTRIBUTING.md#pull-requests)

### License

* [Apache 2](LICENSE.md)
