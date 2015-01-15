# swagger2raml [![Build Status](https://travis-ci.org/8x8Cloud/swagger2raml.svg?branch=master)](https://travis-ci.org/8x8Cloud/swagger2raml) [![Windows Build Status](https://ci.appveyor.com/api/projects/status/github/8x8Cloud/swagger2raml)](https://ci.appveyor.com/project/mmatloka/swagger2raml/build/artifacts) [![Get automatic notifications about new "swagger2raml" versions](https://www.bintray.com/docs/images/bintray_badge_color.png)](https://bintray.com/8x8/maven/swagger2raml/view?source=watch)
A utility to generate [RAML](http://raml.org/) documentation from [Swagger](https://helloreverb.com/developers/swagger) JSON.

## Usage
swagger2raml can be used either from command line (requires Java to be on the `PATH`) or as a Java library.

### Command line
```
java -jar swagger2raml-1.0.0.jar [-o outputFileName] <swaggerApiUrl>
```
where:
- `outputFileName` is an optional parameter to specify the RAML file name (default is `api.raml`),
- `swaggerApiUrl` is the URL of the Swagger API endpoint which provides resource listing as JSON.

### Your application
```java
RamlGenerator.generateFromSwaggerUrl(String url, String outputFileName)
```
method with the parameters as described in the command line usage.

Grab the JAR from Maven (Bintray or Maven Central repository) or use the bundled one:

#### Gradle
```groovy
repositories {
    mavenCentral()
}

compile 'com.8x8.cloud:swagger2raml:1.0.0'
```

#### Maven
```xml
<dependency>
    <groupId>com.8x8.cloud</groupId>
    <artifactId>swagger2raml</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Bundled JAR
Add `swagger2raml-1.0.0-bundled.jar` from [Releases](https://github.com/8x8Cloud/swagger2raml/releases) to your `CLASSPATH`

### Contributing to this project

Anyone and everyone is welcome to contribute. Please take a moment to
review the [guidelines for contributing](CONTRIBUTING.md).

* [Bug reports](CONTRIBUTING.md#bugs)
* [Feature requests](CONTRIBUTING.md#features)
* [Pull requests](CONTRIBUTING.md#pull-requests)

### License

* [Apache 2](LICENSE.md)
