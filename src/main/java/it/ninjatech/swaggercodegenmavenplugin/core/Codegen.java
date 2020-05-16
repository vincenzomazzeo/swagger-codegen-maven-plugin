/*
MIT License

Copyright (c) 2019 Vincenzo Mazzeo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package it.ninjatech.swaggercodegenmavenplugin.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.CodegenSecurity;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.languages.SpringCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.PropertyBuilder.PropertyId;
import io.swagger.util.Json;

/**
 * <p>
 * Extension of the Swagger Spring Codegen. <br>
 * It enables the inheritance of the Model classes from external Model classes and the usage of external Model classes as parameters both in Model and
 * API's.
 * </p>
 * 
 * @author Vincenzo Mazzeo
 * @version 4.0
 * @since 1.0.0
 */
public final class Codegen extends SpringCodegen {

    /** FORCE_JDK8_OFF. */
    protected static final String FORCE_JDK8_OFF = "forceJdk8Off";

    /** APIS_SUFFIX. */
    protected static final String API_SUFFIX = "apiSuffix";

    /** ADD_SECURITY_HEADERS_AS_ARGUMENTS */
    protected static final String SECURITY_HEADERS_AS_ARGUMENTS = "securityHeadersAsArguments";

    /** BASE_PATH_AS_ROOT */
    protected static final String BASE_PATH_AS_ROOT = "basePathAsRoot";

    /** X_TYPE. */
    private static final String X_TYPE = "x-nt-type";

    /** X_SUPER_CLASS. */
    private static final String X_SUPER_CLASS = "x-nt-super-class";

    /** X_INTERFACE_NAME. */
    private static final String X_INTERFACE_NAME = "x-nt-interface-name";

    /** X_TYPE_TEMPLATES. */
    private static final String X_TYPE_TEMPLATES = "x-nt-type-templates";

    /** X_SUPER_CLASS_TEMPLATES. */
    private static final String X_SUPER_CLASS_TEMPLATES = "x-nt-super-class-templates";

    /*
     * (non-Javadoc)
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#getTag()
     */
    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#getName()
     */
    @Override
    public String getName() {
        return "NinjaTech Codegen";
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#getHelp()
     */
    @Override
    public String getHelp() {
        return "Generates Java Interfaces and Models.";
    }

    /**
     * Overridden to set interfaces generation only.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#processOpts()
     */
    @Override
    public void processOpts() {
        setInterfaceOnly(true); // Excludes the Controllers Generation

        super.processOpts();

        if ((boolean) this.additionalProperties.get(FORCE_JDK8_OFF)) {
            this.additionalProperties.remove("jdk8-no-delegate");
            this.additionalProperties.remove("jdk8");
        }
    }

    /**
     * Overridden to handle the extension parameters (x-) for the mapping of the
     * external Model classes.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#getSwaggerType(Property)
     * 
     * @param property
     *            property
     */
    @Override
    public String getSwaggerType(Property property) {
        String swaggerType = super.getSwaggerType(property);

        if ("string".equalsIgnoreCase(swaggerType) && property.getVendorExtensions().containsKey(X_TYPE)) {
            swaggerType = (String) property.getVendorExtensions().get(X_TYPE);
        } else if ("string".equalsIgnoreCase(swaggerType) && property.getFormat() != null && property.getFormat().startsWith("x-")) {
            swaggerType = property.getFormat().substring("x-".length());
        }

        return swaggerType;
    }

    /**
     * Overridden to handle inheritance feature of Model classes and templating.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#postProcessModels(Map)
     * 
     * @param objs
     *            objects
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessModels(Map<String, Object> objs) {
        List<Map<String, ?>> modelMaps = (List<Map<String, ?>>) objs.get("models");

        for (Map<String, ?> modelsMap : modelMaps) {
            CodegenModel model = (CodegenModel) modelsMap.get("model");
            handleSuperClass(model, objs);
            handleTemplateVars(model, objs);
        }

        return super.postProcessModels(objs);
    }

    /**
     * Overridden to handle the usage of the external Model classes as parameter of
     * the API's.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#postProcessOperations(Map)
     * 
     * @param objs
     *            objects
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        Map<String, ?> operationLists = (Map<String, ?>) objs.get("operations");
        List<CodegenOperation> operations = (List<CodegenOperation>) operationLists.get("operation");

        for (CodegenOperation operation : operations) {
            if (this.typeMapping.containsKey(operation.returnBaseType)) {
                operation.returnBaseType = this.typeMapping.get(operation.returnBaseType);
            }
            if (operation.responses != null) {
                for (CodegenResponse response : operation.responses) {
                    if (this.typeMapping.containsKey(response.baseType)) {
                        response.baseType = this.typeMapping.get(response.baseType);
                    }
                }
            }

            handleApiKeySecurityHeaders(operation);

            // In case of external types in body this is the only point where to add the
            // imports.
            if (operation.getHasBodyParam() && operation.bodyParam.vendorExtensions.containsKey(X_TYPE)) {
                CodegenParameter bodyParam = operation.bodyParam;
                addImport(objs, bodyParam.baseType);
            }
        }

        handleBasePathAsRoot(objs);

        return super.postProcessOperations(objs);
    }

    /**
     * Overriden to handle the usage of the external Model classes as body of the
     * API's.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#postProcessParameter(CodegenParameter)
     * 
     * @param parameter
     *            parameter
     */
    @Override
    public void postProcessParameter(CodegenParameter parameter) {
        if (parameter.isBodyParam && StringUtils.isNotBlank(parameter.jsonSchema)) {
            try {
                JsonNode rootNode = Json.mapper().readTree(parameter.jsonSchema);
                JsonNode schemaNode = rootNode.findPath("schema");
                if (!schemaNode.isMissingNode()) {
                    JsonNode xTypeNode = schemaNode.findPath(X_TYPE);
                    if (!xTypeNode.isMissingNode()) {
                        Map<String, Object> vendorExtensions = Collections.singletonMap(X_TYPE, xTypeNode.asText());
                        if (!parameter.isContainer) {
                            Property property = PropertyBuilder.build(parameter.baseType.toLowerCase(), null,
                                                                      Collections.singletonMap(PropertyId.VENDOR_EXTENSIONS, vendorExtensions));
                            String type = getSwaggerType(property);
                            parameter.baseType = type;
                            parameter.dataType = this.typeMapping.get(type);
                        }
                        parameter.isPrimitiveType = true;
                        parameter.vendorExtensions.putAll(vendorExtensions);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        super.postProcessParameter(parameter);
    }

    /**
     * Overriden to handle the API interfaces suffix.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#toApiName(String)
     * 
     * @param name
     *            name
     */
    @Override
    public String toApiName(String name) {
        String result = null;

        if (this.additionalProperties.containsKey(API_SUFFIX)) {
            result = name.length() == 0 ? "Default" : sanitizeName(name);
            result = String.format("%s%s", camelize(result), this.additionalProperties.get(API_SUFFIX));
        } else {
            result = super.toApiName(name);
        }

        return result;
    }

    /**
     * Overriden to handle base path.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#preprocessSwagger(Swagger)
     * 
     * @param swagger
     *            swagger
     */
    @Override
    public void preprocessSwagger(Swagger swagger) {
        super.preprocessSwagger(swagger);

        if ((boolean) this.additionalProperties.get(BASE_PATH_AS_ROOT)
            && StringUtils.isBlank(swagger.getBasePath())) {
            this.additionalProperties.put(BASE_PATH_AS_ROOT, false);
        }
    }

    /**
     * Overriden to change the name of API interfaces.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#fromOperation(String, String, Operation, Map, Swagger)
     * 
     * @param path
     *            path
     * @param httpMethod
     *            httpMethod
     * @param operation
     *            operation
     * @param definitions
     *            definitions
     * @param swagger
     *            swagger
     */
    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions, Swagger swagger) {
        CodegenOperation result = null;

        if (swagger.getPath(path).getVendorExtensions().containsKey(X_INTERFACE_NAME)) {
            operation.setVendorExtension(X_INTERFACE_NAME, swagger.getPath(path).getVendorExtensions().get(X_INTERFACE_NAME));
        }
        result = super.fromOperation(path, httpMethod, operation, definitions, swagger);

        return result;
    }

    /**
     * Overriden to change the name of API interfaces.
     * 
     * @see io.swagger.codegen.languages.SpringCodegen#addOperationToGroup(String, String, Operation, CodegenOperation, Map)
     * 
     * @param tag
     *            tag
     * @param resourcePath
     *            resourcePath
     * @param operation
     *            operation
     * @param co
     *            co
     * @param operations
     *            operations
     */
    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        super.addOperationToGroup(tag,
                                  co.vendorExtensions.containsKey(X_INTERFACE_NAME) ? (String) co.vendorExtensions.get(X_INTERFACE_NAME)
                                                                                    : resourcePath,
                                  operation,
                                  co,
                                  operations);
    }

    /**
     * Model super class management.
     *
     * @param model
     *            model
     * @param objs
     *            objs
     */
    @SuppressWarnings("unchecked")
    private void handleSuperClass(CodegenModel model, Map<String, Object> objs) {
        if (model.vendorExtensions.containsKey(X_SUPER_CLASS)) {
            List<String> superClasses = (List<String>) model.vendorExtensions.get(X_SUPER_CLASS);
            if (superClasses.size() != 1) {
                throw new RuntimeException(String.format("%s extensions must have one and only one value", X_SUPER_CLASS));
            }
            String superClass = superClasses.get(0);
            addImport(objs, superClass);
            model.parent = this.typeMapping.get(superClass);

            if (model.vendorExtensions.containsKey(X_SUPER_CLASS_TEMPLATES)) {
                model.parent = handleTemplates(model.parent, (List<String>) model.vendorExtensions.get(X_SUPER_CLASS_TEMPLATES), objs);
            }
        }
    }

    /**
     * Model parameters with templates management.
     *
     * @param model
     *            model
     * @param objs
     *            objs
     */
    @SuppressWarnings("unchecked")
    private void handleTemplateVars(CodegenModel model, Map<String, Object> objs) {
        List<CodegenProperty> templateVars = model.allVars.stream().filter(e -> e.vendorExtensions.containsKey(X_TYPE_TEMPLATES)).collect(Collectors.toList());
        for (CodegenProperty templateVar : templateVars) {
            String templateType = handleTemplates(templateVar.baseType, (List<String>) templateVar.vendorExtensions.get(X_TYPE_TEMPLATES), objs);
            templateVar.baseType = templateType;
            templateVar.complexType = templateType;
            templateVar.datatype = templateType;
            templateVar.datatypeWithEnum = templateType;
        }
    }

    /**
     * Templates handling.
     *
     * @param type
     *            base type
     * @param templates
     *            templates
     * @param objs
     *            objs
     * @return base type with templates
     */
    private String handleTemplates(String type, List<String> templates, Map<String, Object> objs) {
        String result = null;

        if (!templates.isEmpty()) {
            templates.stream().filter(e -> !e.equals("?")).forEach(e -> addImport(objs, e));
            result = String.format("%s<%s>",
                                   type,
                                   templates.stream().map(e -> this.typeMapping.containsKey(e) ? this.typeMapping.get(e) : e).collect(Collectors.joining(", ")));
        } else {
            result = type;
        }

        return result;
    }

    /**
     * Adds the import of the external Model classes.
     *
     * @param objs
     *            Objects
     * @param alias
     *            Alias of the class to import
     */
    @SuppressWarnings("unchecked")
    private void addImport(Map<String, Object> objs, String alias) {
        List<Map<String, String>> imports = (List<Map<String, String>>) objs.get("imports");
        String aliasFQN = this.importMapping.get(alias);

        if (aliasFQN == null) {
            throw new RuntimeException(String.format("Missing import mapping for %s", alias));
        }

        boolean importAlreadyPresent = false;
        for (Map<String, String> import_ : imports) {
            if (import_.containsValue(aliasFQN)) {
                importAlreadyPresent = true;
                break;
            }
        }

        if (!importAlreadyPresent) {
            imports.add(Collections.singletonMap("import", aliasFQN));
        }
    }

    /**
     * Adds ApiKey Security Headers to the parameters of the method
     * 
     * @param operation
     *            Operation
     */
    private void handleApiKeySecurityHeaders(CodegenOperation operation) {
        if (operation.authMethods != null && (boolean) this.additionalProperties.get(SECURITY_HEADERS_AS_ARGUMENTS)) {
            List<CodegenSecurity> apiKeySecurityHeaders = operation.authMethods.stream().filter(e -> e.isApiKey && e.isKeyInHeader).collect(Collectors.toList());
            if (!apiKeySecurityHeaders.isEmpty()) {
                if (!operation.allParams.isEmpty()) {
                    operation.allParams.get(operation.allParams.size() - 1).hasMore = true;
                }
                for (CodegenSecurity apiKeySecurityHeader : apiKeySecurityHeaders) {
                    CodegenParameter apiKeySecurityHeaderParameter = new CodegenParameter();
                    operation.allParams.add(apiKeySecurityHeaderParameter);
                    operation.headerParams.add(apiKeySecurityHeaderParameter);
                    operation.requiredParams.add(apiKeySecurityHeaderParameter);
                    operation.hasParams = true;
                    operation.hasRequiredParams = true;
                    apiKeySecurityHeaderParameter.baseName = apiKeySecurityHeader.keyParamName;
                    apiKeySecurityHeaderParameter.description = apiKeySecurityHeader.keyParamName;
                    apiKeySecurityHeaderParameter.dataType = "String";
                    apiKeySecurityHeaderParameter.isHeaderParam = true;
                    apiKeySecurityHeaderParameter.isPrimitiveType = true;
                    apiKeySecurityHeaderParameter.isString = true;
                    apiKeySecurityHeaderParameter.paramName = apiKeySecurityHeader.name;
                    apiKeySecurityHeaderParameter.required = true;
                    apiKeySecurityHeaderParameter.hasMore = true;
                }
                operation.allParams.get(operation.allParams.size() - 1).hasMore = false;
            }
        }
    }

    /**
     * Handles the BasePathAsRoot parameter.
     * 
     * @param objs
     *            Objects
     */
    private void handleBasePathAsRoot(Map<String, Object> objs) {
        objs.put(BASE_PATH_AS_ROOT, this.additionalProperties.get(BASE_PATH_AS_ROOT));
    }

}
