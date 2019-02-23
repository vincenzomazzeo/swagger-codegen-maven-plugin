# Swagger Codegen Maven Plugin

The Swagger Codegen Maven Plugin is a Maven Plugin that extends the Spring Codegen implementation of the Swagger Codegen provided by [Smartbear Swagger](https://swagger.io/) configuring it to just generate Model classes and API interfaces and enabling the support for external Model classes.
It is intended to be used in projects that only need Model and API generation.

## Features

- Model classes generation
- API interfaces generation
- Inheritance of the Model classes from external Model classes
- Usage of external Model classes as fields of the internal Model and as parameters/body of the API's

## Build

You'll need a machine with Java 8 and Apache Maven 3 installed.

Checkout:

    git clone git://github.com/vincenzomazzeo/swagger-codegen-maven-plugin.git

Run Build:

    mvn clean install


## Releases

The current release is **1.0.0**.

## Basic usage

- Add the plugin to your POM:

```xml
<plugin>
    <groupId>it.ninjatech</groupId>
	<artifactId>swagger-codegen-maven-plugin</artifactId>
	<version>...</version>
</plugin>
```

- Set the goals:

    - `generate-api` to enable the generation of the API interfaces
    - `generate-model` to enable the generation of the Model classes
  
- Configure the plugin:

The plugin requires only a bunch of configuration parameters:

| Parameter | Type | Description |
|-----------|------|-------------|
| @         | @    | @           |

## Getting involved

To contribute, simply make a pull request and add a brief description (1-2 sentences) of your addition or change. Please note that we aim to keep this project straightforward and focused. We are not looking to add lots of features; we just want it to keep doing what it does, as well and as powerfully as possible.

### Pull requests only

**DON'T** push to the master branch directly. Always use feature branches.

### Guidelines

- **every change** needs a test
- 100% code coverage
- keep the current code style