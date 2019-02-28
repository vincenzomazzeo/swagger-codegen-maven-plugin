# Swagger Codegen Maven Plugin

The Swagger Codegen Maven Plugin is a Maven Plugin that extends the Spring Codegen implementation of the Swagger Codegen provided by [Smartbear Swagger](https://swagger.io/) configuring it to generate just Model classes and API interfaces and enabling the support for external Model classes (classes not generated by the codegen).
It is intended to be used in projects that only need Model and API generation.

## Features

- Model classes generation
- API interfaces generation
- Fixed Swagger Codegen bug [#5614](https://github.com/swagger-api/swagger-codegen/issues/5614)
- Fixed Swagger Codegen bug [#5898](https://github.com/swagger-api/swagger-codegen/issues/5898)
  > Note: Basic fix
- Inheritance of the Model classes from external Model classes.

  It is possible for a Model class to extend an external class specifying the alias of the super class in the extension `x-superClass`:
  ```
    TestModel:
      x-superClass:
        - AbstractModel
      type: object
      properties: [...]
  ```
- Usage of external Model classes as fields of the internal Model and as parameters/response of the API's.

  It is possible for a Model class to have one or more external classes as field type by specifying the type of the field as `string` and the alias of the external type in the extension `x-type`:
  ```
    TestModel:
      [...]
      properties:
        name:
          type: string
        page:
          type: string
          x-type: NTPage
        otherPage:
          type: string
          x-type: NTPage
  ```
  It is possible for an API to use an external class as parameter/response by specifying the type as `string` and the alias of the external type in the extension `x-type`:
  ```
    /testGetModel/page:
      post:
        parameters:
          - name: anomaly
            in: body
            schema:
              type: string
              x-type: NTPage
        responses:
          '200':
            description: successful operation
            schema:
              type: string
              x-type: NTPage
        operationId: testGetPage
        consumes:
          - application/json
        produces:
          - application/json
  ```

## Build

Needs a machine with Java 8 and Apache Maven 3 installed.

Checkout:

    git clone git://github.com/vincenzomazzeo/swagger-codegen-maven-plugin.git

Run Build:

    mvn clean install


## Releases

The current release is **1.0.0**.

## Basic usage

### Plugin

```xml
<plugin>
    <groupId>it.ninjatech</groupId>
	<artifactId>swagger-codegen-maven-plugin</artifactId>
	<version>...</version>
</plugin>
```

### Goals

    - `generate-api` to enable the generation of the API interfaces
    - `generate-model` to enable the generation of the Model classes
  
### Configuration

The plugin requires only a bunch of configuration parameters:

| Parameter         | Type              | Description                                  | Default | Required |
|-------------------|-------------------|----------------------------------------------|---------|----------|
| verbose           | `boolean`         | Enables verbose output                       | `false` | `false`  |
| outputFolder      | `File`            | Generated files output folder                |         | `true`   |
| sourceFiles       | `List<URL>`       | List of URL's of the Swagger YAML files      |         | `true`   |
| apiPackage        | `String`          | Package of the API's interfaces              |         | `false`  |
| modelPackage      | `String`          | Package of the Model classes                 |         | `false`  |
| dataTypeMapping   | `DataTypeMapping` | Data Type Mapping for external Model classes |         | `false`  |
| enableJava8      | `boolean`           | Enable Java 8 support | `false`  | `true`  |
| dateLibrary       | `DateLibrary`      | Library to use for Date & Time | `JAVA8_LOCAL_DATE_TIME`        | `true`  |
| enableBeanValidation | `boolean` | Enable the Validation framework | `true`        | `true`  |
| apiSuffix | `String` | Custom Suffix for API's Interfaces |         | `false`  |
| modelNameSuffix | `String` | Custom Suffix for Model classes |         | `false`  |

The `DataTypeMapping` has the following sections:

- `directMap` to explicitly define the external Model classes by specifying the alias and the fully qualified name.
- `packages` to specify a list of packages to scan. The usage of the '**' wildcard enables the sub packages scanning.
- `externalResources` list of URL's linking to external YAML files containing the mapping.

> Note that:
> - with the packages scanning is not possible to define an alias for the the classes. This means that the name of the class will be used as alias and in case of naming clash (classes with the same name in difference packages) only the latest found class is added. 
> - the packages scanning doesn't work with packages of the project. To use classes of the project it is necessary to declare them with the direct mapping feature.
> - the packages scanning requires that the project(s) containing the packages to be scanned must be added as dependency to the plugin.

The `DateLibrary` can have one of the following values:

- `LEGACY`
- `JAVA8`
- `JAVA8_LOCAL_DATE_TIME`
- `JODA`
- `THREE_TEN`

### Example

```xml
<plugin>
	<groupId>it.ninjatech</groupId>
	<artifactId>swagger-codegen-maven-plugin</artifactId>
	<version>1.0.0</version>
	<executions>
		<execution>
			<goals>
				<goal>generate-api</goal>
				<goal>generate-model</goal>
			</goals>
			<configuration>
				<outputFolder>${project.build.directory}/generated-sources</outputFolder>
				<apiPackage>it.ninjatech.swaggercodegenmavenplugintester.api</apiPackage>
				<modelPackage>it.ninjatech.swaggercodegenmavenplugintester.model</modelPackage>
				<sourceFiles>
					<param>file:${basedir}/src/main/resources/Source.yaml</param>
				</sourceFiles>
				<dataTypeMapping>
					<directMap>
						<NTPage>it.ninjatech.swaggercodegenmavenplugintester.model.Page</NTPage>
						<AbstractModel>it.ninjatech.swaggercodegenmavenplugintester.model.AbstractModel</AbstractModel>
					</directMap>
					<packages>
						<param>it.ninjatech.swaggercodegenmavenplugintestermodel.**</param>
					</packages>
					<externalResources>
						<param>http://localhost/datamapping.yaml</param>
					</externalResources>
				</dataTypeMapping>
			</configuration>
		</execution>
	</executions>
	<dependencies>
		<dependency>
			<groupId>it.ninjatech</groupId>
			<artifactId>swagger-codegen-maven-plugin-tester-model</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>
	</dependencies>
</plugin>
```

## Getting involved

To contribute, simply make a pull request and add a brief description (1-2 sentences) of your addition or change. Please note that we aim to keep this project straightforward and focused. We are not looking to add lots of features; we just want it to keep doing what it does, as well and as powerfully as possible.

### Pull requests only

**DON'T** push to the master branch directly. Always use feature branches.

### Guidelines

- **every change** needs a test
- 100% code coverage
- keep the current code style