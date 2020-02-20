# Swagger Brake [![Build Status](https://travis-ci.com/redskap/swagger-brake.svg?branch=master)](https://travis-ci.com/redskap/swagger-brake)
The main focus of the project is to have a clear view whether any breaking change
is getting introduced with the new version of a Swagger API definition.

The following changes are classified as breaking:
- If any path is removed or the HTTP verb is changed
- If any request parameter is removed
- If any new request parameter is required or an existing one set as required 
- If any request attribute is removed
- If any response attribute is removed

The tool is very useful in case it's necessary not to introduce any backward
incompatible API change unintentionally.

On top of the CLI, there are Maven and Gradle plugins also available for 
easier integration into any existing pipeline.

Maven plugin can be found [here](https://github.com/redskap/swagger-brake-maven-plugin).

Gradle plugin can be found [here](https://github.com/redskap/swagger-brake-gradle).

## Usage
The check can be started by executing the following command:
```bash
$ java -jar swagger-brake.jar --old-api=/home/user/something.yaml --new-api=/home/user/something_v2.yaml [parameters]
```
The available parameters can be checked by executing the help command:
```bash
$ java -jar swagger-brake.jar --help
```
After executing the check, the application will print the results out to the standard out
in the following format:
```text
$ java -jar swagger-brake.jar --old-api=/home/user/petstore.yaml --new-api=/home/user/petstore_v2.yaml
There were breaking API changes

Path /pet/findByStatus GET has been deleted
```

## Reporting
There are multiple output formats for the results. 

Three types are supported:
- standard output
- JSON
- HTML

It can be configured via the `--output-format` argument. The values can be `STDOUT`,`JSON`,`HTML`.
For file types (e.g. `JSON`,`HTML`) it's necessary to set the `--output-path` argument as well, where
to save the results.

Multiple reporters can be set up at the same time by separating the types with commas:
```bash
--output-format=JSON,HTML
```

## API deprecation handling
The [OpenAPI specification](https://swagger.io/specification/#fixed-fields-8) 
defines the `deprecated` attribute on operation level for marking an API deprecated. 
Swagger Brake utilizes this property so that it's possible to delete a deprecated API 
without marking the API broken.

The default behavior is that deprecated APIs can be deleted without any issue but there is
a possibility to override this behavior so that the new API will be considered as broken.

The `--deprecated-api-deletion-allowed` parameter is responsible for setting this behavior
in Swagger Brake. The default value is `true` but it can be overridden to `false` anytime. 

## Latest artifact resolution
For easier CI integration, there is a possibility not to provide the old API path directly 
but to resolve the latest artifact containing the Swagger definition file from any Maven2
repository (including Nexus and Artifactory as well). This feature requires that 
the API definition file is packed into a JAR.

Using the functionality needs 3 parameters:
- `--maven-repo-url`
  - Specifies the repository base URL. Example: `https://oss.jfrog.org/oss-snapshot-local`
- `--groupId`
  - The groupId of the artifact 
- `--artifactId`
  - The artifactId

Example command:
```bash
$ java -jar swagger-brake.jar --new-api=/home/user/petstore_v2.yaml --maven-repo-url=https://oss.jfrog.org/oss-snapshot-local --groupId=com.example --artifactId=petstore-api
```

#### Secured Maven repository
It's also possible that the repository is secured with username and password. The following
2 parameters can be used to provide the credentials to access the repository:
- `--maven-repo-username`
- `--maven-repo-password`

#### Implementation details
The mechanism under the hood is to resolve the latest artifact based on the `maven-metadata.xml`
for a given groupId and artifactId. After the latest version has been determined, it will be 
downloaded to the temp directory. The downloaded JAR will be scanned for any of the following 
files which will be used for providing the old API:
- `swagger.json`
- `swagger.yaml`
- `swagger.yml`

## Running with Docker
This repo contains a `Dockerfile` for running `swagger-brake` in a container context.

```bash
$ docker build -t redskap/swagger-brake .
$ docker run -v $(pwd):/app -it redskap/swagger-brake --old-api=petstore.yaml --new-api=petstore_v2.yaml  

```

## Building
The application is using Gradle as a build system and building it can be done 
by executing the following command:
```bash
$ ./gradlew clean build
```

## License
```text
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```