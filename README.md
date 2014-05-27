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

### Java library
Add `dist/swagger2raml.jar` to your `CLASSPATH` and use the
```java
RamlGenerator.generateFromSwaggerUrl(String url, String outputFileName)
```
method with the parameters as described in the command line usage.

## Issue guidelines

This repository's [CONTRIBUTING.md](CONTRIBUTING.md) provides a generic set of
guidelines for developers to adapt and include in their own GitHub
repositories, thereby taking advantage of [GitHub's integration with
contributing guidelines](https://github.com/blog/1184-contributing-guidelines).

The section below can be included in your README to improve the visibility of
your contribution guidelines.

If you think this guide can be improved, please let me know.


### Contributing to this project

Anyone and everyone is welcome to contribute. Please take a moment to
review the [guidelines for contributing](CONTRIBUTING.md).

* [Bug reports](CONTRIBUTING.md#bugs)
* [Feature requests](CONTRIBUTING.md#features)
* [Pull requests](CONTRIBUTING.md#pull-requests)
