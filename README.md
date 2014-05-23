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
