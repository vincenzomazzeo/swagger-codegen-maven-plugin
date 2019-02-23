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

import org.codehaus.plexus.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.languages.SpringCodegen;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.PropertyBuilder.PropertyId;
import io.swagger.util.Json;

/**
 * <p>
 * Extension of the Swagger Spring Codegen.
 * <br />
 * It enables the inheritance of the Model classes from external Model classes
 * and the usage of external Model classes as parameters both in Model and
 * API's.
 * </p>
 * 
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public final class Codegen extends SpringCodegen {

	/** The Constant X_TYPE. */
	private static final String X_TYPE = "x-type";

	/** The Constant X_SUPER_CLASS. */
	private static final String X_SUPER_CLASS = "x-superClass";

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
	 * Overridden to set the generation of only the interfaces.
	 */
	@Override
	public void processOpts() {
		setInterfaceOnly(true); // Excludes the Controllers Generation

		super.processOpts();
	}

	/**
	 * Overridden to handle the extension parameters (x-) for the mapping of the
	 * external Model classes.
	 */
	@Override
	public String getSwaggerType(Property property) {
		String swaggerType = super.getSwaggerType(property);

		if ("string".equalsIgnoreCase(swaggerType) && property.getVendorExtensions().containsKey(X_TYPE)) {
			swaggerType = (String) property.getVendorExtensions().get(X_TYPE);
		}
		else if ("string".equalsIgnoreCase(swaggerType) && property.getFormat() != null && property.getFormat().startsWith("x-")) {
			swaggerType = property.getFormat().substring("x-".length());
		}

		return swaggerType;
	}

	/**
	 * Overridden to handle the inheritance feature of the Model classes.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> postProcessModels(Map<String, Object> objs) {
		// Super Class Management
		List<Map<String, ?>> modelMaps = (List<Map<String, ?>>) objs.get("models");

		for (Map<String, ?> modelsMap : modelMaps) {
			CodegenModel model = (CodegenModel) modelsMap.get("model");
			if (model.vendorExtensions.containsKey(X_SUPER_CLASS)) {
				List<String> superClasses = (List<String>) model.vendorExtensions.get(X_SUPER_CLASS);
				if (superClasses.size() != 1) {
					throw new RuntimeException(String.format("%s extensions must have one and only one value", X_SUPER_CLASS));
				}
				String superClass = superClasses.get(0);
				addImport(objs, superClass);
				model.parent = superClass;
			}
		}

		return super.postProcessModels(objs);
	}

	/**
	 * Overridden to handle the usage of the external Model classes as parameter of
	 * the API's.
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

			// In case of external types in body this is the only point where to add the
			// imports.
			if (operation.getHasBodyParam() && operation.bodyParam.vendorExtensions.containsKey(X_TYPE)) {
				CodegenParameter bodyParam = operation.bodyParam;
				addImport(objs, bodyParam.baseType);
			}
		}

		return super.postProcessOperations(objs);
	}

	/**
	 * Overriden to handle the usage of the external Model classes as body of the
	 * API's.
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
						Property property = PropertyBuilder.build(parameter.baseType.toLowerCase(), null, Collections.singletonMap(PropertyId.VENDOR_EXTENSIONS, vendorExtensions));
						String type = getSwaggerType(property);
						parameter.baseType = type;
						parameter.dataType = this.typeMapping.get(type);
						parameter.isPrimitiveType = true;
						parameter.vendorExtensions.putAll(vendorExtensions);
					}
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		super.postProcessParameter(parameter);
	}

	/**
	 * Adds the import of the external Model classes.
	 *
	 * @param Objects
	 * @param Alias of the class to import
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

}
